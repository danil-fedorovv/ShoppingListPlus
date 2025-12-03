package danilfedorov.core_data.repository

import danilfedorov.core_data.local.datasource.datastore.PremiumDataStore
import danilfedorov.domain_core.repository.FruitAlbumRepository
import danilfedorov.domain_core.repository.PremiumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class PremiumRepositoryImpl(
    private val premiumDataStore: PremiumDataStore,
    private val fruitAlbumRepository: FruitAlbumRepository
) : PremiumRepository {

    override val isPremiumFlow: Flow<Boolean> = premiumDataStore.isPremiumFlow.onEach {
        if(it) {
            fruitAlbumRepository.unlockAllLockedImages()
        }
    }

    override suspend fun refreshPremiumState() {
        premiumDataStore.refreshPremiumState()
    }

    override suspend fun getExpirationDate(): Long? {
        return premiumDataStore.getExpirationDate()
    }
}