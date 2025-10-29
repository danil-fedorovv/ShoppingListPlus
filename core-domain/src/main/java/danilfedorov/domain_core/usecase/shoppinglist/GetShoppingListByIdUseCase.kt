package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.repository.ShoppingListRepository

class GetShoppingListByIdUseCase(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(listId: Long): ShoppingList =
        repository.getShoppingListById(listId)
}