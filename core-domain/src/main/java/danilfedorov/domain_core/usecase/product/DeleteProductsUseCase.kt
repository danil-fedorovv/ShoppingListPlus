package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository

class DeleteProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(ids: List<Long>) = repository.deleteProducts(ids)
}