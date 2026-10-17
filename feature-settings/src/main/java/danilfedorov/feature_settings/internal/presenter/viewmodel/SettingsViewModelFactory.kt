package danilfedorov.feature_settings.internal.presenter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_settings.api.SettingsDeps
import danilfedorov.feature_settings.internal.di.DaggerSettingsComponent

internal class SettingsViewModelFactory(
    private val navBack: () -> Unit,
    private val deps: SettingsDeps,
    private val context: Context,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = DaggerSettingsComponent.factory().create(context, deps)

        @Suppress("UNCHECKED_CAST")
        return component.settingsViewModelFactory.create(
            navBack = navBack,
        ) as T
    }
}