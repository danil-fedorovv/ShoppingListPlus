package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository
import javax.inject.Inject

class SelectAllProductsUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(remoteListId: String?, shoppingListId: Long) =
        repository.setAllProductsSelection(remoteListId, shoppingListId, true)
}