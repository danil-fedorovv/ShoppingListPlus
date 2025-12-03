package danilfedorov.domain_core.usecase.fruitalbum

import danilfedorov.domain_core.repository.FruitAlbumRepository

class ToggleFruitEnabledUseCase(private val repository: FruitAlbumRepository) {
    suspend operator fun invoke(index: Int) {
        repository.toggleImageEnabled(index)
    }
}