package danilfedorov.feature_settings.internal.di

import dagger.Module
import dagger.Provides
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.AppearanceRepository
import danilfedorov.domain_core.usecase.appearance.ChangeColorIndexUseCase
import danilfedorov.domain_core.usecase.appearance.ChangeThemeUseCase
import danilfedorov.domain_core.usecase.appearance.ColorIndexFlowUseCase
import danilfedorov.domain_core.usecase.appearance.ThemeFlowUseCase

@Module
object UseCaseModule {

    @Provides
    @FeatureScope
    fun provideColorIndexFlowUseCase(appearanceRepository: AppearanceRepository): ColorIndexFlowUseCase {
        return ColorIndexFlowUseCase(appearanceRepository)
    }

    @Provides
    @FeatureScope
    fun provideThemeFlowUseCase(appearanceRepository: AppearanceRepository): ThemeFlowUseCase {
        return ThemeFlowUseCase(appearanceRepository)
    }


    @Provides
    @FeatureScope
    fun provideSetColorIndex(appearanceRepository: AppearanceRepository): ChangeColorIndexUseCase {
        return ChangeColorIndexUseCase(appearanceRepository)
    }

    @Provides
    @FeatureScope
    fun provideChangeThemeUseCase(appearanceRepository: AppearanceRepository): ChangeThemeUseCase {
        return ChangeThemeUseCase(appearanceRepository)
    }

}