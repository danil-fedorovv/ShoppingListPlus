package danilfedorov.domain_core.usecase.appearance

import danilfedorov.domain_core.repository.AppearanceRepository

class ChangeColorIndexUseCase(private val repository: AppearanceRepository) {
    suspend operator fun invoke(index: Int) = repository.setColorIndex(index)
}