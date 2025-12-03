package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class ObserveConnectionStatusUseCase(
    private val repository: ProductRepository,
) {
    operator fun invoke(): Flow<Boolean> =
        repository.observeConnectionStatus().catch { emit(false) }
}