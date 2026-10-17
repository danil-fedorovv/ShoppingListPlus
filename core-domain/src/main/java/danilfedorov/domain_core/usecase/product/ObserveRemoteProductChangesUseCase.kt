package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRemoteProductChangesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(remoteListId: String, shoppingListId: Long): Flow<Unit> {
        return repository.observeRemoteChanges(remoteListId, shoppingListId)
    }
}