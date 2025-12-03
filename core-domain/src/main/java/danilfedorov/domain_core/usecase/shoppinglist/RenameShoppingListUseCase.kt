package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository


class RenameShoppingListUseCase(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(remoteListId: String?, id: Long, newName: String) = runCatching {
        repository.renameShoppingList(remoteListId, id, newName)
    }
}