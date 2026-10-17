package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProductListByListIdUseCase @Inject constructor(private val repository: ProductRepository) {

    operator fun invoke(listId: Long): Flow<List<Product>> {
        return repository.observeProductsByListId(listId)
    }

}