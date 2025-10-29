package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository

class ArchiveShoppingListsUseCase(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(ids: List<Long>) {
        val timestamp = System.currentTimeMillis()
        repository.archiveShoppingLists(ids, timestamp)
    }
}