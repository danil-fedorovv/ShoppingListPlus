package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository
import javax.inject.Inject

class ChangeSortByAlphabetUseCase @Inject constructor(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(listId: Long, enabled: Boolean) =
        repository.setSortByAlphabet(listId, enabled)
}