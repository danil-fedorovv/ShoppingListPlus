package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository

class UnselectAllProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(remoteListId: String?, shoppingListId: Long) =
        repository.setAllProductsSelection(remoteListId, shoppingListId, false)
}