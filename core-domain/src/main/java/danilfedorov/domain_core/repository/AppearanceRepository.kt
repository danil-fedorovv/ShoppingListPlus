package danilfedorov.domain_core.repository

import kotlinx.coroutines.flow.Flow

interface AppearanceRepository {
    val themeFlow: Flow<Boolean?>
    val colorIndexFlow: Flow<Int>
    suspend fun setTheme(isDarkTheme: Boolean)
    suspend fun setColorIndex(index: Int)
}