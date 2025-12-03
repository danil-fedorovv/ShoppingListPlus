package danilfedorov.domain_core.repository

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList
import kotlinx.coroutines.flow.Flow


interface ShoppingListRepository {
    suspend fun createShoppingList(list: ShoppingList): Long

    fun getShoppingLists(): List<ShoppingList>
    fun observeShoppingListById(id: Long): Flow<ShoppingList>
    fun observeShoppingLists(): Flow<List<ShoppingList>>
    fun observeArchivedShoppingLists(): Flow<List<ShoppingList>>
    suspend fun getRemoteListName(remoteListId: String): String
    suspend fun getLocalListIdByRemoteId(remoteListId: String): Long?

    suspend fun renameShoppingList(remoteListId: String?, id: Long, newName: String)
    suspend fun setSortByAlphabet(listId: Long, enabled: Boolean)
    suspend fun archiveShoppingLists(ids: List<Long>, deletedAt: Long)
    suspend fun restoreShoppingList(id: Long)
    suspend fun updateRemoteListId(id: Long, remoteListId: String)

    suspend fun deleteShoppingList(ids: List<Long>)

    suspend fun pushList(list: ShoppingList, products: List<Product>): String
    suspend fun fetchList(remoteListId: String): Pair<ShoppingList, List<Product>>
    fun observeRemoteListName(remoteListId: String): Flow<Unit>

}