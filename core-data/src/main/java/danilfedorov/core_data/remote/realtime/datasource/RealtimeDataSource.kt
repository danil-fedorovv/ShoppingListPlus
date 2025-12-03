package danilfedorov.core_data.remote.realtime.datasource

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.model.firebase.ProductEvent
import danilfedorov.domain_core.model.firebase.ProductField
import kotlinx.coroutines.flow.Flow

interface RealtimeDataSource {

    suspend fun pushList(list: ShoppingList, items: List<Product>): String
    suspend fun fetchList(remoteListId: String): Pair<ShoppingList, List<Product>>

    fun updateProductField(
        remoteListId: String,
        firebaseProductId: String,
        field: ProductField,
        value: Any
    )
    fun addProduct(remoteListId: String, product: Product): String
    fun deleteProducts(remoteListId: String, productIds: List<String>)
    fun updateAllProductsSelection(remoteListId: String, isSelected: Boolean)
    fun observeProductEvents(listId: String): Flow<ProductEvent>

    suspend fun getListName(remoteListId: String): String
    fun updateListName(remoteListId: String, newName: String)
    fun observeListName(remoteListId: String): Flow<String>

    fun observeConnectionStatus(): Flow<Boolean>
}