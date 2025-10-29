package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository

class GetProductListByListIdUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: Long): List<Product> = repository.getProductsByListId(id)
}