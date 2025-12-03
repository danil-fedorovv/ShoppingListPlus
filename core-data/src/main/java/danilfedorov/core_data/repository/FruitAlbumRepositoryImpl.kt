package danilfedorov.core_data.repository

import danilfedorov.core_data.local.datasource.datastore.FruitAlbumDataStore
import danilfedorov.domain_core.repository.FruitAlbumRepository
import kotlinx.coroutines.flow.Flow

class FruitAlbumRepositoryImpl(
    private val dataStore: FruitAlbumDataStore
) : FruitAlbumRepository {

    override val imagesFlow: Flow<Map<Int, Pair<Boolean, Boolean>>> = dataStore.imagesFlow

    override suspend fun unlockRandomImage(): Int {
        return dataStore.unlockRandomImage()
    }

    override suspend fun getRandomActiveUnlockedImage(): Int {
        return dataStore.getRandomActiveUnlockedImage()
    }

    override suspend fun toggleImageEnabled(index: Int) {
        dataStore.toggleImageEnabled(index)
    }

    override suspend fun unlockAllLockedImages() {
        dataStore.unlockAllLockedImages()
    }
}