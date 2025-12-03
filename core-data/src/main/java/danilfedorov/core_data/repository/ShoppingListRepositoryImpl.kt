package danilfedorov.core_data.repository

import danilfedorov.core_data.local.datasource.room.dao.ShoppingListDao
import danilfedorov.core_data.local.datasource.room.mapper.ShoppingListMapper
import danilfedorov.core_data.remote.realtime.datasource.FirebaseRealtimeDataSource
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.repository.ShoppingListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingListRepositoryImpl(
    private val dao: ShoppingListDao,
    private val mapper: ShoppingListMapper,
    private val firebaseRealtimeDataSource: FirebaseRealtimeDataSource,
) : ShoppingListRepository {

    override suspend fun createShoppingList(list: ShoppingList): Long {
        val entity = mapper.mapToEntity(list)
        return dao.insertShoppingList(entity)
    }

    override fun getShoppingLists(): List<ShoppingList> {
        return mapper.mapToModels(dao.getShoppingListsUseCase())
    }

    override fun observeShoppingListById(id: Long): Flow<ShoppingList> {
        return dao.observeShoppingListById(id)
            .map { entity -> mapper.mapToModel(entity) }
    }

    override fun observeShoppingLists(): Flow<List<ShoppingList>> {
        return dao.observeShoppingLists()
            .map { entities -> mapper.mapToModels(entities) }
    }

    override fun observeArchivedShoppingLists(): Flow<List<ShoppingList>> {
        return dao.observeArchivedShoppingLists()
            .map { entities -> mapper.mapToModels(entities) }
    }

    override suspend fun getRemoteListName(remoteListId: String): String {
        return firebaseRealtimeDataSource.getListName(remoteListId)
    }

    override suspend fun getLocalListIdByRemoteId(remoteListId: String): Long? {
        return dao.getLocalListIdByRemoteId(remoteListId)
    }

    override suspend fun renameShoppingList(remoteListId: String?, id: Long, newName: String) {
        coroutineScope {
            launch { dao.renameShoppingList(id, newName) }
            remoteListId?.let {
                launch { firebaseRealtimeDataSource.updateListName(it, newName) }
            }
        }
    }

    override suspend fun setSortByAlphabet(listId: Long, enabled: Boolean) {
        dao.updateSortByAlphabet(listId, enabled)
    }

    override suspend fun archiveShoppingLists(
        ids: List<Long>,
        deletedAt: Long,
    ) {
        dao.archiveShoppingLists(ids, deletedAt)
    }

    override suspend fun restoreShoppingList(id: Long) {
        dao.restoreShoppingList(id)
    }

    override suspend fun updateRemoteListId(id: Long, remoteListId: String) {
        dao.updateRemoteListId(id, remoteListId)
    }

    override suspend fun deleteShoppingList(ids: List<Long>) {
        dao.deleteShoppingLists(ids)
    }

    override suspend fun pushList(
        list: ShoppingList,
        products: List<Product>,
    ): String {
        return firebaseRealtimeDataSource.pushList(list, products)
    }

    override suspend fun fetchList(remoteListId: String): Pair<ShoppingList, List<Product>> {
        return firebaseRealtimeDataSource.fetchList(remoteListId)
    }

    override fun observeRemoteListName(remoteListId: String): Flow<Unit> {
        return firebaseRealtimeDataSource.observeListName(remoteListId)
            .onEach {
                withContext(Dispatchers.IO) {
                    val localId = dao.getLocalListIdByRemoteId(remoteListId) ?: return@withContext
                    dao.renameShoppingList(localId, it)
                }
            }
            .catch { }
            .map {}
    }

}