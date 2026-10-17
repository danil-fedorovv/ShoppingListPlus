package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository
import javax.inject.Inject


class RenameShoppingListUseCase @Inject constructor(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(remoteListId: String?, id: Long, newName: String) = runCatching {
        repository.renameShoppingList(remoteListId, id, newName)
    }
}