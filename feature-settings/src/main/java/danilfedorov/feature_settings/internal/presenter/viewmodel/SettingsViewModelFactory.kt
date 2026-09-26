package danilfedorov.feature_settings.internal.presenter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_settings.api.SettingsDeps
import danilfedorov.feature_settings.internal.di.DaggerSettingsComponent

internal class SettingsViewModelFactory(
    private val navBack: () -> Unit,
    private val deps: SettingsDeps,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = DaggerSettingsComponent.factory().create(context, deps)
        return SettingsViewModel(
            navBack,
            component.themeFlowUseCase,
            component.colorIndexFlowUseCase,
            component.changeColorIndexUseCase,
            component.changeThemeUseCase,
            component.adManager
        ) as T
    }
}