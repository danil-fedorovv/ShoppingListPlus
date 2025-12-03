package danilfedorov.core_data.local.datasource.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class FruitAlbumDataStore(private val context: Context) {

    private val Context.dataStore by preferencesDataStore("image_settings")

    companion object {
        private fun unlockedKey(index: Int) = booleanPreferencesKey("image_${index}_unlocked")
        private fun enabledKey(index: Int) = booleanPreferencesKey("image_${index}_enabled")

        private const val TOTAL_IMAGES = 22
    }

    val imagesFlow: Flow<Map<Int, Pair<Boolean, Boolean>>> = context.dataStore.data.map { prefs ->
        (0 until TOTAL_IMAGES).associateWith { index ->
            val locked = prefs[unlockedKey(index)] ?: (index == 0)
            val enabled = prefs[enabledKey(index)] ?: (index == 0)
            locked to enabled
        }
    }

    suspend fun unlockRandomImage(): Int {
        val currentMap = imagesFlow.first()

        val lockedIndexes = currentMap
            .filter { !it.value.first }
            .keys
            .toList()

        if (lockedIndexes.isEmpty()) {
            return -1
        }

        val randomIndex = lockedIndexes.random()

        context.dataStore.edit { prefs ->
            prefs[unlockedKey(randomIndex)] = true
            prefs[enabledKey(randomIndex)] = true
        }

        return randomIndex
    }

    suspend fun getRandomActiveUnlockedImage(): Int {
        val map = imagesFlow.first()
        val activeUnlocked = map.filter { it.value.first && it.value.second }.keys
        return activeUnlocked.random()
    }

    suspend fun toggleImageEnabled(index: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[enabledKey(index)] ?: false
            prefs[enabledKey(index)] = !current
        }
    }

    suspend fun unlockAllLockedImages() {
        val lockedIndexes = imagesFlow.first()
            .filterNot { it.value.first }
            .keys

        if (lockedIndexes.isNotEmpty()) {
            context.dataStore.edit { prefs ->
                lockedIndexes.forEach { index ->
                    prefs[unlockedKey(index)] = true
                    prefs[enabledKey(index)] = true
                }
            }
        }
    }
}