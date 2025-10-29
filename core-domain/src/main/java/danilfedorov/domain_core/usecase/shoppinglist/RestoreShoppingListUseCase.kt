package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository

class RestoreShoppingListUseCase(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(id: Long) = repository.restoreShoppingList(id)
}