package danilfedorov.domain_core.usecase.appearance

import danilfedorov.domain_core.repository.AppearanceRepository

class ChangeThemeUseCase(private val repository: AppearanceRepository) {
    suspend operator fun invoke(defaultTheme: Boolean) = repository.setTheme(defaultTheme)
}