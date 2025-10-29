package danilfedorov.domain_core.usecase.appearance

import danilfedorov.domain_core.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow

class ThemeFlowUseCase(private val repository: AppearanceRepository) {
    operator fun invoke(): Flow<Boolean?> = repository.themeFlow
}