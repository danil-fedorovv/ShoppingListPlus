package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository

class GetShoppingListProgressUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(shoppingListId: Long): String {
        val selectedCount = repository.getSelectedProductCount(shoppingListId)
        val totalCount = repository.getTotalProductCount(shoppingListId)
        return "$selectedCount/$totalCount"
    }
}