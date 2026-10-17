package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository
import javax.inject.Inject

class ArchiveShoppingListsUseCase @Inject constructor(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(ids: List<Long>) {
        val timestamp = System.currentTimeMillis()
        repository.archiveShoppingLists(ids, timestamp)
    }
}