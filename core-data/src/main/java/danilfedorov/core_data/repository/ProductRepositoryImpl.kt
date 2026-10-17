package danilfedorov.core_data.repository

import com.google.firebase.database.FirebaseDatabase
import danilfedorov.core_data.local.datasource.room.dao.ProductDao
import danilfedorov.core_data.local.datasource.room.mapper.ProductMapper
import danilfedorov.core_data.remote.realtime.datasource.FirebaseRealtimeDataSource
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.firebase.ProductEvent
import danilfedorov.domain_core.model.firebase.ProductField
import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val dao: ProductDao,
    private val mapper: ProductMapper,
    private val firebaseRealtimeDataSource: FirebaseRealtimeDataSource,
) : ProductRepository {

    private val addedMutex = Mutex()

    override suspend fun addProduct(remoteListId: String?, item: Product): Long = coroutineScope {
        val localId = dao.insertProduct(mapper.mapToEntity(item.copy(firebaseId = null)))

        remoteListId?.let { remoteId ->
            launch {
                runCatching {
                    val firebaseId = firebaseRealtimeDataSource.addProduct(remoteId, item)
                    dao.updateFirebaseId(localId, firebaseId)
                }
            }
        }
        localId
    }

    override suspend fun addProducts(items: List<Product>) {
        val entities = mapper.mapToEntities(items)
        dao.insertProducts(entities)
    }

    override suspend fun getProductsByListId(shoppingListId: Long): List<Product> {
        val entityList = dao.getProductsByListId(shoppingListId)
        return mapper.mapToModels(entityList)
    }

    override fun observeProductsByListId(shoppingListId: Long): Flow<List<Product>> {
        return dao.observeProductsByListId(shoppingListId)
            .map { entities -> mapper.mapToModels(entities) }
    }

    override fun getSelectedProductCountFlow(shoppingListId: Long): Flow<Int> =
        dao.getSelectedProductCountFlow(shoppingListId)

    override suspend fun getSelectedProductCount(shoppingListId: Long): Int =
        dao.getSelectedProductCount(shoppingListId)

    override fun getTotalProductCountFlow(shoppingListId: Long): Flow<Int> =
        dao.getTotalProductCountFlow(shoppingListId)

    override suspend fun getTotalProductCount(shoppingListId: Long): Int =
        dao.getTotalProductCount(shoppingListId)

    override suspend fun updateProductSelection(
        remoteListId: String?,
        product: Product,
        isSelected: Boolean,
    ) {
        coroutineScope {
            launch { dao.updateProductSelection(product.roomId, isSelected) }

            remoteListId?.let { remoteId ->
                product.firebaseId?.let { firebaseId ->
                    launch(Dispatchers.IO) {
                        FirebaseDatabase.getInstance()
                            .getReference("shopping_lists/$remoteId/items/$firebaseId/selected")
                            .setValue(isSelected)
                    }
                }
            }
        }
    }

    override suspend fun setAllProductsSelection(
        remoteListId: String?,
        shoppingListId: Long,
        isSelected: Boolean,
    ) {
        coroutineScope {
            launch { dao.updateAllProductsSelection(shoppingListId, isSelected) }

            remoteListId?.let { remoteId ->
                async {
                    runCatching {
                        firebaseRealtimeDataSource.updateAllProductsSelection(remoteId, isSelected)
                    }.isSuccess
                }
            }
        }
    }

    override suspend fun updateAmountAndUnit(
        remoteListId: String?,
        product: Product,
        amount: String?,
        unit: String?,
    ) {
        coroutineScope {
            launch { dao.updateProductAmountAndUnit(product.roomId, amount, unit) }

            remoteListId?.let { remoteId ->
                product.firebaseId?.let { firebaseId ->

                    amount?.let { amt ->
                        launch(Dispatchers.IO) {
                            FirebaseDatabase.getInstance()
                                .getReference("shopping_lists/$remoteId/items/$firebaseId/amount")
                                .setValue(amt)
                        }
                    }

                    unit?.let { unt ->
                        launch(Dispatchers.IO) {
                            FirebaseDatabase.getInstance()
                                .getReference("shopping_lists/$remoteId/items/$firebaseId/unit")
                                .setValue(unt)
                        }
                    }
                }
            }
        }
    }

    override suspend fun updateProductName(
        remoteListId: String?,
        product: Product,
        newName: String,
    ) {
        coroutineScope {
            launch { dao.updateProductName(product.roomId, newName) }
            remoteListId?.let { remoteListId ->
                product.firebaseId?.let { firebaseId ->
                    launch {
                        runCatching {
                            firebaseRealtimeDataSource.updateProductField(
                                remoteListId, firebaseId, ProductField.NAME, newName
                            )
                        }.isSuccess
                    }
                }
            }
        }
    }

    override suspend fun deleteProducts(remoteListId: String?, products: List<Product>) {
        coroutineScope {
            launch { dao.deleteProducts(products.map { it.roomId }) }

            remoteListId?.let { remoteId ->
                val firebaseIds = products.mapNotNull { it.firebaseId }
                if (firebaseIds.isEmpty()) return@let null

                launch { firebaseRealtimeDataSource.deleteProducts(remoteId, firebaseIds) }
            }
        }
    }

    override fun observeRemoteChanges(remoteListId: String, shoppingListId: Long): Flow<Unit> =
        channelFlow<Unit> {
            val changedJobs = mutableMapOf<String, Job>()

            firebaseRealtimeDataSource.observeProductEvents(remoteListId)
                .collect { event ->
                    when (event) {
                        is ProductEvent.Added -> {
                            val incoming = event.product
                            val firebaseId = incoming.firebaseId ?: return@collect

                            addedMutex.withLock {
                                withContext(Dispatchers.IO) {
                                    if (dao.existsByFirebaseId(firebaseId)) return@withContext

                                    val existingProduct = dao.getProductByDetails(
                                        listId = shoppingListId,
                                        name = incoming.name,
                                        amount = incoming.amount,
                                        unit = incoming.unit
                                    )

                                    if (existingProduct != null) {
                                        dao.updateFirebaseId(existingProduct.id, firebaseId)
                                    } else {
                                        val entity = mapper.mapToEntity(
                                            incoming.copy(
                                                listId = shoppingListId,
                                                firebaseId = firebaseId
                                            )
                                        )
                                        dao.upsertProduct(entity)
                                    }
                                }
                            }
                        }

                        is ProductEvent.Changed -> {
                            val fbId = event.product.firebaseId ?: return@collect
                            changedJobs[fbId]?.cancel()
                            changedJobs[fbId] = launch {
                                delay(150)
                                handleChanged(event)
                                changedJobs.remove(fbId)
                            }
                        }

                        is ProductEvent.Removed -> {
                            event.product.firebaseId?.let { firebaseId ->
                                withContext(Dispatchers.IO) {
                                    dao.deleteByFirebaseId(firebaseId)
                                }
                            }
                        }
                    }
                }
        }.map { }

    private suspend fun handleChanged(event: ProductEvent.Changed) {
        val incoming = event.product
        val fbId = incoming.firebaseId ?: return

        withContext(Dispatchers.IO) {
            val existing = dao.getProductByFirebaseId(fbId)
            if (existing != null) {
                val updated = existing.copy(
                    name = incoming.name,
                    amount = incoming.amount,
                    unit = incoming.unit,
                    isSelected = incoming.isSelected
                )
                dao.upsertProduct(updated)
            }
        }
    }

    override fun observeConnectionStatus(): Flow<Boolean> =
        firebaseRealtimeDataSource.observeConnectionStatus()
}