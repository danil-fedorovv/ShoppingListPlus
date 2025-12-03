package danilfedorov.domain_core.usecase.fruitalbum

import danilfedorov.domain_core.repository.FruitAlbumRepository

class GetRandomActiveUnlockedFruitUseCase(private val repository: FruitAlbumRepository) {
    suspend operator fun invoke(): Int = repository.getRandomActiveUnlockedImage()
}