package danilfedorov.domain_core.repository

import kotlinx.coroutines.flow.Flow

interface PremiumRepository {
    val isPremiumFlow: Flow<Boolean>
    suspend fun refreshPremiumState()
    suspend fun getExpirationDate(): Long?
}