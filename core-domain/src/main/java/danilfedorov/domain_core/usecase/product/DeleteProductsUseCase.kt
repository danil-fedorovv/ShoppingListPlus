package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository

class DeleteProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(remoteListId: String?, products: List<Product>) =
        repository.deleteProducts(remoteListId, products)
}