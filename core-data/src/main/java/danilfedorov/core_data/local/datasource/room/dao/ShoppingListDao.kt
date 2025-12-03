package danilfedorov.core_data.local.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import danilfedorov.core_data.local.datasource.room.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Insert
    suspend fun insertShoppingList(shoppingListEntity: ShoppingListEntity): Long

    @Query("select * from lists where id = :listId")
    fun observeShoppingListById(listId: Long): Flow<ShoppingListEntity>

    @Query("select * from lists where isArchived = 0")
    fun observeShoppingLists(): Flow<List<ShoppingListEntity>>

    @Query("select * from lists where isArchived = 1")
    fun observeArchivedShoppingLists(): Flow<List<ShoppingListEntity>>

    @Query("select * from lists where isArchived = 0")
    fun getShoppingListsUseCase(): List<ShoppingListEntity>

    @Query("update lists set isArchived = 1, deletedAt = :deletedAt, remoteListId = null where id in (:ids) ")
    suspend fun archiveShoppingLists(ids: List<Long>, deletedAt: Long)

    @Query("update lists set isArchived = 0, deletedAt = null where id = :id")
    suspend fun restoreShoppingList(id: Long)

    @Query("delete from lists where id in (:ids)")
    suspend fun deleteShoppingLists(ids: List<Long>)

    @Query("update lists set name = :newName where id = :id")
    suspend fun renameShoppingList(id: Long, newName: String)

    @Query("update lists set isSortedByAlphabet = :enabled where id = :id")
    suspend fun updateSortByAlphabet(id: Long, enabled: Boolean)

    @Query("update lists set remotelistid = :remoteListId where id = :id")
    suspend fun updateRemoteListId(id: Long, remoteListId: String)

    @Query("select id from lists where remoteListId = :remoteListId and isArchived = 0 LIMIT 1")
    suspend fun getLocalListIdByRemoteId(remoteListId: String): Long?
}