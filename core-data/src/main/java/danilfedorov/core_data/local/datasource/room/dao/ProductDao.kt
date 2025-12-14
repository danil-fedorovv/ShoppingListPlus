package danilfedorov.core_data.local.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import danilfedorov.core_data.local.datasource.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(item: ProductEntity): Long

    @Query("select * from items where listId = :shoppingListId")
    suspend fun getProductsByListId(shoppingListId: Long): List<ProductEntity>

    @Insert
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: ProductEntity)

    @Query("select * from items where listId = :shoppingListId")
    fun observeProductsByListId(shoppingListId: Long): Flow<List<ProductEntity>>

    @Query("select count(*) from items where listId = :shoppingListId and isSelected = 1")
    fun getSelectedProductCountFlow(shoppingListId: Long): Flow<Int>

    @Query("select count(*) from items where listId = :shoppingListId and isSelected = 1")
    suspend fun getSelectedProductCount(shoppingListId: Long): Int

    @Query("select count(*) from items where listId = :shoppingListId")
    fun getTotalProductCountFlow(shoppingListId: Long): Flow<Int>

    @Query("select count(*) from items where listId = :shoppingListId")
    suspend fun getTotalProductCount(shoppingListId: Long): Int

    @Query("select * from items where firebaseId = :firebaseId")
    suspend fun getProductByFirebaseId(firebaseId: String): ProductEntity?

    @Query("select exists(select 1 from items where firebaseId = :firebaseId)")
    suspend fun existsByFirebaseId(firebaseId: String): Boolean

    @Query("update items set isSelected = :isSelected where id = :id")
    suspend fun updateProductSelection(id: Long, isSelected: Boolean)

    @Query("update items set isSelected = :isSelected where listId = :shoppingListId")
    suspend fun updateAllProductsSelection(shoppingListId: Long, isSelected: Boolean)

    @Query("update items set amount = :amount, unit = :unit where id = :id")
    suspend fun updateProductAmountAndUnit(id: Long, amount: String?, unit: String?)

    @Query("update items set name = :newName where id = :id")
    suspend fun updateProductName(id: Long, newName: String)

    @Query("update items set firebaseId = :firebaseId where id = :roomId")
    suspend fun updateFirebaseId(roomId: Long, firebaseId: String)

    @Query("update items set firebaseId = :firebaseId where listId = :shoppingListId")
    suspend fun updateFirebaseIds(shoppingListId: Long, firebaseId: String)

    @Query("DELETE FROM items WHERE id IN (:ids)")
    suspend fun deleteProducts(ids: List<Long>)

    @Query("DELETE FROM items WHERE firebaseId = :firebaseId")
    suspend fun deleteByFirebaseId(firebaseId: String)

    @Query("""
    SELECT * FROM items 
    WHERE listId = :listId 
    AND name = :name 
    AND amount = :amount 
    AND unit = :unit
    LIMIT 1
""")
    suspend fun getProductByDetails(
        listId: Long,
        name: String,
        amount: String,
        unit: String
    ): ProductEntity?
}