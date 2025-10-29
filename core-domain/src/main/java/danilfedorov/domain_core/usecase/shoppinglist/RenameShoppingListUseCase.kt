package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository


class RenameShoppingListUseCase(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(id: Long, newName: String) =
        repository.renameShoppingList(id, newName)
}