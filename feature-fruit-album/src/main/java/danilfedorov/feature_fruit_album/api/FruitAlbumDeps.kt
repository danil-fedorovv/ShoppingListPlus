package danilfedorov.feature_fruit_album.api

import danilfedorov.domain_core.repository.FruitAlbumRepository

interface FruitAlbumDeps {
    val fruitAlbumRepository: FruitAlbumRepository
    val yandexRewardedKey: String
}