package danilfedorov.domain_core.usecase.appearance

import danilfedorov.domain_core.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeFlowUseCase @Inject constructor(private val repository: AppearanceRepository) {
    operator fun invoke(): Flow<Boolean?> = repository.themeFlow
}