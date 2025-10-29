package danilfedorov.feature_settings.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.domain_core.usecase.appearance.ColorIndexFlowUseCase
import danilfedorov.domain_core.usecase.appearance.ThemeFlowUseCase
import danilfedorov.domain_core.usecase.appearance.ChangeColorIndexUseCase
import danilfedorov.domain_core.usecase.appearance.ChangeThemeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    themeFlowUseCase: ThemeFlowUseCase,
    colorIndexFlowUseCase: ColorIndexFlowUseCase,
    private val changeColorIndexUseCase: ChangeColorIndexUseCase,
    private val changeThemeUseCase: ChangeThemeUseCase
): ViewModel() {

    val darkTheme: Flow<Boolean?> = themeFlowUseCase()

    val colorIndex: Flow<Int?> = colorIndexFlowUseCase()

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            changeThemeUseCase(enabled)
        }
    }

    fun setColorIndex(index: Int) {
        viewModelScope.launch {
            changeColorIndexUseCase(index)
        }
    }
}