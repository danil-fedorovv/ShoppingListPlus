package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.repository.ShoppingListRepository

class GetArchivedShoppingListsUseCase(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(): List<ShoppingList> = repository.getArchivedShoppingLists()
}