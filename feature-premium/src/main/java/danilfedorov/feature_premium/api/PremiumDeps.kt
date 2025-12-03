package danilfedorov.feature_premium.api

import danilfedorov.domain_core.repository.FruitAlbumRepository
import danilfedorov.domain_core.repository.PremiumRepository

interface PremiumDeps {
    val premiumRepository: PremiumRepository
    val fruitAlbumRepository: FruitAlbumRepository
}