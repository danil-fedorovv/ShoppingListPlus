package danilfedorov.feature_settings.internal.di

import android.content.Context
import dagger.Module
import dagger.Provides
import danilfedorov.common.ad.AdManager
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
    fun provideColorIndexFlowUseCase(appearanceRepository: AppearanceRepository) =
        ColorIndexFlowUseCase(appearanceRepository)


    @Provides
    @FeatureScope
    fun provideThemeFlowUseCase(appearanceRepository: AppearanceRepository) =
        ThemeFlowUseCase(appearanceRepository)


    @Provides
    @FeatureScope
    fun provideSetColorIndex(appearanceRepository: AppearanceRepository) =
        ChangeColorIndexUseCase(appearanceRepository)


    @Provides
    @FeatureScope
    fun provideChangeThemeUseCase(appearanceRepository: AppearanceRepository) =
        ChangeThemeUseCase(appearanceRepository)


    @Provides
    @FeatureScope
    fun provideAdManager(yandexRewardKey: String, context: Context): AdManager = AdManager(yandexRewardKey, context)
}