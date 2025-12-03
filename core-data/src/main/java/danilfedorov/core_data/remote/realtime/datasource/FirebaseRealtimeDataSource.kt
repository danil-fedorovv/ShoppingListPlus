package danilfedorov.core_data.remote.realtime.datasource

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.ServerValue
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import danilfedorov.core_data.remote.realtime.model.FirebaseKeys
import danilfedorov.core_data.remote.realtime.model.FirebaseProduct
import danilfedorov.core_data.remote.realtime.model.FirebaseShoppingList
import danilfedorov.core_data.remote.realtime.toProduct
import danilfedorov.domain_core.exceptions.FirebaseKeyGenerationException
import danilfedorov.domain_core.exceptions.FirebasePushFailedException
import danilfedorov.domain_core.exceptions.RemoteListNotFoundException
import danilfedorov.domain_core.exceptions.TimeoutException
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.model.firebase.ProductEvent
import danilfedorov.domain_core.model.firebase.ProductField
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class FirebaseRealtimeDataSource : RealtimeDataSource {

    init {
        runCatching {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
    }

    override suspend fun pushList(list: ShoppingList, items: List<Product>): String = try {
        withTimeout(TIMEOUT) {
            val ref = FirebaseDatabase.getInstance()
                .getReference(FirebaseKeys.SHOPPING_LISTS)
                .push()

            val itemsRef = ref.child(FirebaseKeys.ShoppingList.ITEMS)

            val itemsMap = items.associate { product ->
                val itemRef = itemsRef.push()
                val firebaseId = itemRef.key ?: throw FirebaseKeyGenerationException()

                val firebaseProduct = FirebaseProduct(
                    firebaseId = firebaseId,
                    name = product.name,
                    amount = product.amount,
                    unit = product.unit,
                    selected = product.isSelected
                )

                itemRef.setValue(firebaseProduct).await()

                firebaseId to firebaseProduct
            }

            val firebaseList = FirebaseShoppingList(
                name = list.name,
                createdAt = ServerValue.TIMESTAMP,
                items = itemsMap
            )

            ref.setValue(firebaseList).await()

            ref.key ?: throw FirebasePushFailedException()
        }
    } catch (_: TimeoutCancellationException) {
        throw TimeoutException()
    }

    override suspend fun fetchList(remoteListId: String): Pair<ShoppingList, List<Product>> {
        val listRef = FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$remoteListId")

        val nameSnapshot = listRef.child(FirebaseKeys.ShoppingList.NAME).get().await()
        if (!nameSnapshot.exists()) throw RemoteListNotFoundException()

        val list = ShoppingList(
            name = nameSnapshot.getValue(String::class.java) ?: throw RemoteListNotFoundException(),
            remoteListId = remoteListId
        )

        val itemsSnapshot = try {
            withTimeout(TIMEOUT) {
                listRef.child(FirebaseKeys.ShoppingList.ITEMS).get().await()
            }
        } catch (_: Exception) {
            throw TimeoutException()
        }

        val products = itemsSnapshot.children.mapNotNull { child ->
            val item = child.getValue(FirebaseProduct::class.java) ?: return@mapNotNull null

            Product(
                firebaseId = item.firebaseId,
                listId = 0,
                name = item.name,
                amount = item.amount,
                unit = item.unit,
                isSelected = item.selected
            )
        }

        return list to products
    }

    override fun updateProductField(
        remoteListId: String,
        firebaseProductId: String,
        field: ProductField,
        value: Any,
    ) {
        FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$remoteListId/${FirebaseKeys.ShoppingList.ITEMS}/$firebaseProductId/${field.firebaseKey}")
            .setValue(value)
    }

    override fun addProduct(remoteListId: String, product: Product): String {
        val ref = FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$remoteListId/${FirebaseKeys.ShoppingList.ITEMS}")
            .push()

        val firebaseId = ref.key ?: throw FirebaseKeyGenerationException()

        val firebaseProduct = FirebaseProduct(
            firebaseId = firebaseId,
            name = product.name,
            amount = product.amount,
            unit = product.unit,
            selected = product.isSelected
        )

        ref.setValue(firebaseProduct)

        return firebaseId
    }

    override fun deleteProducts(remoteListId: String, productIds: List<String>) {
        if (productIds.isEmpty()) return

        val ref = FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$remoteListId/${FirebaseKeys.ShoppingList.ITEMS}")

        productIds.forEach { id ->
            ref.child(id).removeValue()
        }
    }

    override fun updateAllProductsSelection(remoteListId: String, isSelected: Boolean) {
        val itemsRef = FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$remoteListId/${FirebaseKeys.ShoppingList.ITEMS}")

        itemsRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                if (!currentData.hasChildren()) {
                    return Transaction.success(currentData)
                }

                for (child in currentData.children) {
                    child.child(FirebaseKeys.Product.SELECTED).value = isSelected
                }

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?,
            ) {
                error?.let { }
            }
        })
    }

    override fun observeProductEvents(listId: String): Flow<ProductEvent> =
        FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$listId/${FirebaseKeys.ShoppingList.ITEMS}")
            .let { ref ->
                callbackFlow {
                    val listener = object : ChildEventListener {
                        override fun onChildAdded(sn: DataSnapshot, prev: String?) {
                            sn.toProduct()?.let { trySend(ProductEvent.Added(it)) }
                        }

                        override fun onChildChanged(sn: DataSnapshot, prev: String?) {
                            sn.toProduct()?.let { trySend(ProductEvent.Changed(it)) }
                        }

                        override fun onChildRemoved(sn: DataSnapshot) {
                            sn.toProduct()?.let { trySend(ProductEvent.Removed(it)) }
                        }

                        override fun onChildMoved(sn: DataSnapshot, prev: String?) {}

                        override fun onCancelled(error: DatabaseError) {
                            close(error.toException())
                        }
                    }

                    ref.addChildEventListener(listener)
                    awaitClose { ref.removeEventListener(listener) }
                }
            }

    override suspend fun getListName(remoteListId: String): String {
        val nameRef = FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$remoteListId/${FirebaseKeys.ShoppingList.NAME}")

        val snapshot = try {
            withTimeout(TIMEOUT) {
                nameRef.get().await()
            }
        } catch (e: Exception) {
            throw TimeoutException()
        }

        if (snapshot.value == null) {
            throw RemoteListNotFoundException()
        }

        return snapshot.getValue(String::class.java)!!
    }

    override fun updateListName(remoteListId: String, newName: String) {
        FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$remoteListId/${FirebaseKeys.ShoppingList.NAME}")
            .setValue(newName)
    }

    override fun observeListName(remoteListId: String): Flow<String> =
        FirebaseDatabase.getInstance()
            .getReference("${FirebaseKeys.SHOPPING_LISTS}/$remoteListId/${FirebaseKeys.ShoppingList.NAME}")
            .let { ref ->
                callbackFlow {
                    val listener = object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            try {
                                snapshot.getValue(String::class.java)?.let { trySend(it) }
                            } catch (_: Exception) {

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            close(error.toException())
                        }
                    }

                    ref.addValueEventListener(listener)
                    awaitClose { ref.removeEventListener(listener) }
                }
            }

    override fun observeConnectionStatus(): Flow<Boolean> =
        FirebaseDatabase.getInstance()
            .getReference(".info/connected")
            .let { connectedRef ->
                callbackFlow {
                    val listener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val isConnected = snapshot.getValue(Boolean::class.java) ?: false
                            trySend(isConnected)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            close(error.toException())
                        }
                    }

                    connectedRef.addValueEventListener(listener)
                    awaitClose { connectedRef.removeEventListener(listener) }
                }
            }

    companion object {
        private const val TIMEOUT = 15_000L
    }
}

