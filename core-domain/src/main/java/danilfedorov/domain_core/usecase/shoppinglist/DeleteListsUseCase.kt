package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository

class DeleteListsUseCase(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(ids: List<Long>) = repository.deleteShoppingList(ids)
}