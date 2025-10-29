package danilfedorov.core_data.impl

import danilfedorov.core_data.datasource.datastore.AppearanceDataStore
import danilfedorov.domain_core.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow


class AppearanceRepositoryImpl(
    private val appearanceDataStore: AppearanceDataStore
): AppearanceRepository {

    override val themeFlow: Flow<Boolean>
        get() = appearanceDataStore.themeFlow

    override val colorIndexFlow: Flow<Int>
        get() = appearanceDataStore.colorIndexFlow

    override suspend fun setTheme(isDarkTheme: Boolean) {
        return appearanceDataStore.setTheme(isDarkTheme)
    }

    override suspend fun setColorIndex(index: Int) {
        appearanceDataStore.setColorIndex(index)
    }

}