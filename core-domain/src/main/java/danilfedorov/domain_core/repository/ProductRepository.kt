package danilfedorov.domain_core.repository

import danilfedorov.domain_core.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun createProduct(item: Product): Long

    suspend fun getProductsByListId(shoppingListId: Long): List<Product>
    suspend fun getSelectedProductCount(shoppingListId: Long): Int
    suspend fun getTotalProductCount(shoppingListId: Long): Int
    fun getSelectedProductCountFlow(shoppingListId: Long): Flow<Int>
    fun getTotalProductCountFlow(shoppingListId: Long): Flow<Int>

    suspend fun updateProductSelection(id: Long, isSelected: Boolean)
    suspend fun unselectAllProducts(shoppingListId: Long)
    suspend fun selectAllProducts(shoppingListId: Long)
    suspend fun updateAmountAndUnit(id: Long, amount: String?, unit: String?)
    suspend fun updateProductName(id: Long, newName: String)

    suspend fun deleteProducts(ids: List<Long>)
}