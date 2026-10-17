package danilfedorov.feature_settings.internal.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.feature_settings.api.SettingsDeps
import danilfedorov.feature_settings.internal.presenter.viewmodel.SettingsViewModel

@Component(
    dependencies = [SettingsDeps::class],
    modules = [AdModule::class]
)
@FeatureScope
internal interface SettingsComponent {

    val settingsViewModelFactory: SettingsViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, deps: SettingsDeps): SettingsComponent
    }
}