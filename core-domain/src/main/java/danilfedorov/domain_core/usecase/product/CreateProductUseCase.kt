package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository

class CreateProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(item: Product): Long = repository.createProduct(item)
}