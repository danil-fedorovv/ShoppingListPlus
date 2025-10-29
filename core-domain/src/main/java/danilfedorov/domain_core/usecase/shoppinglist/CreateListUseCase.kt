package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.repository.ShoppingListRepository

class CreateListUseCase(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(name: String): Long = repository.createShoppingList(ShoppingList(name = name))
}