package danilfedorov.feature_settings.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_settings.api.SettingsDeps
import danilfedorov.feature_settings.internal.di.DaggerSettingsComponent

internal class SettingsViewModelFactory(
    private val deps: SettingsDeps
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = DaggerSettingsComponent.factory().create(deps)
        return SettingsViewModel(
            component.themeFlowUseCase,
            component.colorIndexFlowUseCase,
            component.changeColorIndexUseCase,
            component.changeThemeUseCase
        ) as T
    }
}