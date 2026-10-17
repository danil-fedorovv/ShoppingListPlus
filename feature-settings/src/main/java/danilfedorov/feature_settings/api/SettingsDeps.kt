package danilfedorov.feature_settings.api

import danilfedorov.domain_core.repository.AppearanceRepository
import danilfedorov.feature_settings.internal.di.YandexRewardedKey

interface SettingsDeps {
    val appearanceRepository: AppearanceRepository

    @get:YandexRewardedKey
    val yandexRewardedKey: String
}