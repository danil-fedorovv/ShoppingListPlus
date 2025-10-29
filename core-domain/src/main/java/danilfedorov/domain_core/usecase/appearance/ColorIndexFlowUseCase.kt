package danilfedorov.domain_core.usecase.appearance

import danilfedorov.domain_core.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow

class ColorIndexFlowUseCase(private val repository: AppearanceRepository) {
    operator fun invoke(): Flow<Int?> = repository.colorIndexFlow
}