package danilfedorov.core_data.datasource.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppearanceDataStore(private val context: Context) {

    private val Context.dataStore by preferencesDataStore("settings")

    companion object {
        private val KEY_THEME = booleanPreferencesKey("KEY_THEME")
        private val KEY_COLOR = intPreferencesKey("KEY_COLOR")
    }

    val themeFlow: Flow<Boolean?> = context.dataStore.data.map {
        it[KEY_THEME]
    }

    val colorIndexFlow: Flow<Int> = context.dataStore.data.map {
        it[KEY_COLOR] ?: 0
    }


    suspend fun setTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME] = isDarkTheme
        }
    }

    suspend fun setColorIndex(index: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_COLOR] = index
        }
    }

}