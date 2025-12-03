package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ObserveProductListByListIdUseCase(private val repository: ProductRepository) {

    operator fun invoke(listId: Long): Flow<List<Product>> {
        return repository.observeProductsByListId(listId)
    }

}