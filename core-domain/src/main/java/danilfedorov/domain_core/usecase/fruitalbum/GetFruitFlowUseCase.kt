package danilfedorov.domain_core.usecase.fruitalbum

import danilfedorov.domain_core.repository.FruitAlbumRepository

class GetFruitFlowUseCase(private val repository: FruitAlbumRepository) {
    operator fun invoke() = repository.imagesFlow
}