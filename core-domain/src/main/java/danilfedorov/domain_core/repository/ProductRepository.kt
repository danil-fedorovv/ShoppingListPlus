package danilfedorov.domain_core.repository

import danilfedorov.domain_core.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun addProduct(remoteListId: String?, item: Product): Long
    suspend fun addProducts(items: List<Product>)

    suspend fun getProductsByListId(shoppingListId: Long): List<Product>
    fun observeProductsByListId(shoppingListId: Long): Flow<List<Product>>
    suspend fun getSelectedProductCount(shoppingListId: Long): Int
    suspend fun getTotalProductCount(shoppingListId: Long): Int
    fun getSelectedProductCountFlow(shoppingListId: Long): Flow<Int>
    fun getTotalProductCountFlow(shoppingListId: Long): Flow<Int>

    suspend fun updateProductSelection(remoteListId: String?, product: Product, isSelected: Boolean)
    suspend fun setAllProductsSelection(remoteListId: String?, shoppingListId: Long, isSelected: Boolean)
    suspend fun updateAmountAndUnit(remoteListId: String?, product: Product, amount: String?, unit: String?)

    suspend fun updateProductName(remoteListId: String?, product: Product, newName: String)

    suspend fun deleteProducts(remoteListId: String?, products: List<Product>)

    fun observeRemoteChanges(remoteListId: String, shoppingListId: Long):  Flow<Unit>
    fun observeConnectionStatus(): Flow<Boolean>
}
