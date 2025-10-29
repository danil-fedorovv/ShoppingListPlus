package danilfedorov.domain_core.repository

import danilfedorov.domain_core.model.ShoppingList


interface ShoppingListRepository {
    suspend fun createShoppingList(list: ShoppingList): Long

    suspend fun getShoppingLists(): List<ShoppingList>
    suspend fun getArchivedShoppingLists(): List<ShoppingList>
    suspend fun getShoppingListById(listId: Long): ShoppingList

    suspend fun renameShoppingList(id: Long, newName: String)
    suspend fun setSortByAlphabet(listId: Long, enabled: Boolean)
    suspend fun archiveShoppingLists(ids: List<Long>, deletedAt: Long)
    suspend fun restoreShoppingList(id: Long)

    suspend fun deleteShoppingList(ids: List<Long>)
}