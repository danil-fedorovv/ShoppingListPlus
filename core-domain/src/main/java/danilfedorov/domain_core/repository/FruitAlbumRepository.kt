package danilfedorov.domain_core.repository

import kotlinx.coroutines.flow.Flow


interface FruitAlbumRepository {
    val imagesFlow: Flow<Map<Int, Pair<Boolean, Boolean>>>
    suspend fun unlockRandomImage(): Int
    suspend fun getRandomActiveUnlockedImage(): Int
    suspend fun toggleImageEnabled(index: Int)
    suspend fun unlockAllLockedImages()
}