package danilfedorov.domain_core.usecase.fruitalbum

import danilfedorov.domain_core.repository.FruitAlbumRepository

class UnlockRandomFruitUseCase(private val repository: FruitAlbumRepository) {
    suspend operator fun invoke() = repository.unlockRandomImage()
}
