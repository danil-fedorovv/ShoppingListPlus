package danilfedorov.feature_settings.api

import danilfedorov.domain_core.repository.AppearanceRepository

interface SettingsDeps {
    val appearanceRepository: AppearanceRepository
}