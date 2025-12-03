package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.repository.ShoppingListRepository

class GetShoppingListsUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(): List<ShoppingList> = repository.getShoppingLists()
}