package danilfedorov.core_data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import danilfedorov.core_data.datasource.room.entity.ProductEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(item: ProductEntity): Long

    @Query("select * from items where listId = :shoppingListId")
    suspend fun getProductsByListId(shoppingListId: Long): List<ProductEntity>

    @Query("select count(*) from items where listId = :shoppingListId and isSelected = 1")
    fun getSelectedProductCountFlow(shoppingListId: Long): Flow<Int>

    @Query("select count(*) from items where listId = :shoppingListId and isSelected = 1")
    suspend fun getSelectedProductCount(shoppingListId: Long): Int

    @Query("select count(*) from items where listId = :shoppingListId")
    fun getTotalProductCountFlow(shoppingListId: Long): Flow<Int>

    @Query("select count(*) from items where listId = :shoppingListId")
    suspend fun getTotalProductCount(shoppingListId: Long): Int

    @Query("update items set isSelected = :isSelected where id = :id")
    suspend fun updateProductSelection(id: Long, isSelected: Boolean)

    @Query("update items set isSelected = 0 where listId = :shoppingListId")
    suspend fun unselectAllProducts(shoppingListId: Long)

    @Query("update items set isSelected = 1 where listId = :shoppingListId")
    suspend fun selectAllProducts(shoppingListId: Long)

    @Query("update items set amount = :amount, unit = :unit where id = :id")
    suspend fun updateProductAmountAndUnit(id: Long, amount: String?, unit: String?)

    @Query("update items set name = :newName where id = :id")
    suspend fun updateProductName(id: Long, newName: String)

    @Delete
    suspend fun deleteProduct(item: ProductEntity)

    @Query("DELETE FROM items WHERE id IN (:ids)")
    suspend fun deleteProducts(ids: List<Long>)

    @Query("delete from items where listId = :shoppingListId and isSelected = 1 ")
    suspend fun deleteSelectedProducts(shoppingListId: Long)
}