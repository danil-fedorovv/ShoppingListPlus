package danilfedorov.core_data.impl

import danilfedorov.core_data.datasource.room.dao.ProductDao
import danilfedorov.core_data.datasource.room.mapper.ProductMapper
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val dao: ProductDao,
    private val mapper: ProductMapper
) : ProductRepository {

    override suspend fun createProduct(item: Product): Long {
        val entity = mapper.mapToEntity(item)
        return dao.insertProduct(entity)
    }

    override suspend fun getProductsByListId(shoppingListId: Long): List<Product> {
        val entityList = dao.getProductsByListId(shoppingListId)
        return mapper.mapToModels(entityList)
    }

    override fun getSelectedProductCountFlow(shoppingListId: Long): Flow<Int> =
        dao.getSelectedProductCountFlow(shoppingListId)

    override suspend fun getSelectedProductCount(shoppingListId: Long): Int =
        dao.getSelectedProductCount(shoppingListId)

    override fun getTotalProductCountFlow(shoppingListId: Long): Flow<Int> =
        dao.getTotalProductCountFlow(shoppingListId)

    override suspend fun getTotalProductCount(shoppingListId: Long): Int =
        dao.getTotalProductCount(shoppingListId)

    override suspend fun updateProductSelection(id: Long, isSelected: Boolean) =
        dao.updateProductSelection(id, isSelected)

    override suspend fun unselectAllProducts(shoppingListId: Long) =
        dao.unselectAllProducts(shoppingListId)

    override suspend fun selectAllProducts(shoppingListId: Long) =
        dao.selectAllProducts(shoppingListId)

    override suspend fun updateAmountAndUnit(id: Long, amount: String?, unit: String?) =
        dao.updateProductAmountAndUnit(id, amount, unit)

    override suspend fun updateProductName(id: Long, newName: String) =
        dao.updateProductName(id, newName)

    override suspend fun deleteProducts(ids: List<Long>) {
        dao.deleteProducts(ids)
    }
}