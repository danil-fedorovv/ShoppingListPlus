package danilfedorov.feature_settings.internal.di

import android.content.Context
import dagger.Module
import dagger.Provides
import danilfedorov.common.ad.AdManager
import danilfedorov.common.di.FeatureScope

@Module
internal object AdModule {

    @Provides
    @FeatureScope
    fun provideAdManager(@YandexRewardedKey yandexRewardKey: String, context: Context): AdManager =
        AdManager(yandexRewardKey, context)
}