package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.flow.Flow


class GetSelectedProductCountUseCase(private val repository: ProductRepository) {
    operator fun invoke(id: Long): Flow<Int> = repository.getSelectedProductCountFlow(id)
}