package danilfedorov.core_data.impl

import danilfedorov.core_data.datasource.room.dao.ShoppingListDao
import danilfedorov.core_data.datasource.room.mapper.ShoppingListMapper
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.repository.ShoppingListRepository

class ShoppingListRepositoryImpl(
    private val dao: ShoppingListDao,
    private val mapper: ShoppingListMapper
) : ShoppingListRepository {

    override suspend fun createShoppingList(list: ShoppingList): Long {
        val entity = mapper.mapToEntity(list)
        return dao.insertShoppingList(entity)
    }

    override suspend fun getShoppingLists(): List<ShoppingList> {
        return mapper.mapToModels(dao.getShoppingLists())
    }

    override suspend fun getArchivedShoppingLists(): List<ShoppingList> {
        return mapper.mapToModels(dao.getArchivedShoppingLists())
    }

    override suspend fun getShoppingListById(listId: Long): ShoppingList {
        return mapper.mapToModel(dao.getShoppingListById(listId))
    }

    override suspend fun renameShoppingList(id: Long, newName: String) {
        dao.renameShoppingList(id, newName)
    }

    override suspend fun setSortByAlphabet(listId: Long, enabled: Boolean) {
        dao.updateSortByAlphabet(listId, enabled)
    }

    override suspend fun archiveShoppingLists(
        ids: List<Long>,
        deletedAt: Long
    ) {
        dao.archiveShoppingLists(ids, deletedAt)
    }

    override suspend fun restoreShoppingList(id: Long) {
        dao.restoreShoppingList(id)
    }

    override suspend fun deleteShoppingList(ids: List<Long>) {
        dao.deleteShoppingLists(ids)
    }
}