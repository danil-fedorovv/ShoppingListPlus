package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ObserveRemoteProductChangesUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(remoteListId: String, shoppingListId: Long): Flow<Unit> {
        return repository.observeRemoteChanges(remoteListId, shoppingListId)
    }
}