package danilfedorov.core_data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import danilfedorov.core_data.datasource.room.entity.ShoppingListEntity

@Dao
interface ShoppingListDao {

    @Insert
    suspend fun insertShoppingList(shoppingListEntity: ShoppingListEntity): Long

    @Query("select * from lists where isArchived = 0")
    suspend fun getShoppingLists(): List<ShoppingListEntity>

    @Query("select * from lists where isArchived = 1")
    suspend fun getArchivedShoppingLists(): List<ShoppingListEntity>

    @Query("update lists set isArchived = 1, deletedAt = :deletedAt where id in (:ids) ")
    suspend fun archiveShoppingLists(ids: List<Long>, deletedAt: Long)

    @Query("update lists set isArchived = 0, deletedAt = null where id = :id")
    suspend fun restoreShoppingList(id: Long)

    @Query("delete from lists where id in (:ids)")
    suspend fun deleteShoppingLists(ids: List<Long>)

    @Query("update lists set name = :newName where id = :id")
    suspend fun renameShoppingList(id: Long, newName: String)

    @Query("update lists set isSortedByAlphabet = :enabled where id = :id")
    suspend fun updateSortByAlphabet(id: Long, enabled: Boolean)

    @Query("select * from lists where id = :id")
    suspend fun getShoppingListById(id: Long): ShoppingListEntity
}