package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository

class UnselectAllProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(shoppingListId: Long) =
        repository.unselectAllProducts(shoppingListId)
}