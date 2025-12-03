package danilfedorov.domain_core.usecase.premium

import danilfedorov.domain_core.repository.PremiumRepository
import kotlinx.coroutines.flow.Flow

class PremiumStatusFlowUseCase(
    private val repository: PremiumRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isPremiumFlow
}