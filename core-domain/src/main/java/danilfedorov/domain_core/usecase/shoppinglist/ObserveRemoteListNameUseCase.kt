package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow

class ObserveRemoteListNameUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(remoteListId: String): Flow<Unit> =
        repository.observeRemoteListName(remoteListId)
}