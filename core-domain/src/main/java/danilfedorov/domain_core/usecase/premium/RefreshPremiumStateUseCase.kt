package danilfedorov.domain_core.usecase.premium

import danilfedorov.domain_core.repository.PremiumRepository

class RefreshPremiumStateUseCase(
    private val repository: PremiumRepository
) {
    suspend operator fun invoke() {
        repository.refreshPremiumState()
    }
}