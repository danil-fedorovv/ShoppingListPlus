package danilfedorov.feature_settings.internal.di

import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.appearance.ChangeColorIndexUseCase
import danilfedorov.domain_core.usecase.appearance.ChangeThemeUseCase
import danilfedorov.domain_core.usecase.appearance.ColorIndexFlowUseCase
import danilfedorov.domain_core.usecase.appearance.ThemeFlowUseCase
import danilfedorov.feature_settings.api.SettingsDeps

@Component(
    dependencies = [SettingsDeps::class],
    modules = [UseCaseModule::class]
)
@FeatureScope
interface SettingsComponent {

    val themeFlowUseCase: ThemeFlowUseCase
    val colorIndexFlowUseCase: ColorIndexFlowUseCase
    val changeColorIndexUseCase: ChangeColorIndexUseCase
    val changeThemeUseCase: ChangeThemeUseCase

    @Component.Factory
    interface Factory {
        fun create(deps: SettingsDeps): SettingsComponent
    }
}