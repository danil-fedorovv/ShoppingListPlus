package danilfedorov.feature_settings.api

import danilfedorov.domain_core.repository.AppearanceRepository
import danilfedorov.domain_core.repository.PremiumRepository

interface SettingsDeps {
    val appearanceRepository: AppearanceRepository
    val premiumRepository: PremiumRepository
    val yandexRewardedKey: String
}