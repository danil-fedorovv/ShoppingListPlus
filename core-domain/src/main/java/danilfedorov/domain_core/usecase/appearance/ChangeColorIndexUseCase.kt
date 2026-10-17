package danilfedorov.domain_core.usecase.appearance

import danilfedorov.domain_core.repository.AppearanceRepository
import javax.inject.Inject

class ChangeColorIndexUseCase @Inject constructor(private val repository: AppearanceRepository) {
    suspend operator fun invoke(index: Int) = repository.setColorIndex(index)
}