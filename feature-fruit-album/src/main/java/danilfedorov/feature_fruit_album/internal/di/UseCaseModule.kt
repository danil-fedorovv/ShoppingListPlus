package danilfedorov.feature_fruit_album.internal.di

import android.content.Context
import dagger.Module
import dagger.Provides
import danilfedorov.common.ad.AdManager
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.FruitAlbumRepository
import danilfedorov.domain_core.usecase.fruitalbum.GetFruitFlowUseCase
import danilfedorov.domain_core.usecase.fruitalbum.ToggleFruitEnabledUseCase
import danilfedorov.domain_core.usecase.fruitalbum.UnlockRandomFruitUseCase

@Module
object UseCaseModule {

    @Provides
    @FeatureScope
    fun provideGetFruitFlowUseCase(repository: FruitAlbumRepository) =
        GetFruitFlowUseCase(repository)

    @Provides
    @FeatureScope
    fun provideUnlockFruitUseCase(repository: FruitAlbumRepository) =
        UnlockRandomFruitUseCase(repository)

    @Provides
    @FeatureScope
    fun provideToggleFruitEnabledUseCase(repository: FruitAlbumRepository) =
        ToggleFruitEnabledUseCase(repository)


    @Provides
    @FeatureScope
    fun provideAdManager(yandexRewardKey: String, context: Context): AdManager = AdManager(yandexRewardKey, context)

}