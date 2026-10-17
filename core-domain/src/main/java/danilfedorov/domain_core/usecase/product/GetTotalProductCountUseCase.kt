package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTotalProductCountUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(id: Long): Flow<Int> = repository.getTotalProductCountFlow(id)
}