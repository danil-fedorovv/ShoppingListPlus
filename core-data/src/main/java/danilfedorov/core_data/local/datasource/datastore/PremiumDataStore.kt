package danilfedorov.core_data.local.datasource.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.rustore.sdk.pay.PurchaseInteractor
import ru.rustore.sdk.pay.model.ProductType
import ru.rustore.sdk.pay.model.SubscriptionPurchase
import ru.rustore.sdk.pay.model.SubscriptionPurchaseStatus

class PremiumDataStore(
    private val context: Context,
    private val purchaseInteractor: PurchaseInteractor,
) {

    companion object {
        private val Context.dataStore by preferencesDataStore("premium_prefs")

        private val KEY_SUB = booleanPreferencesKey("KEY_SUB")
        private val KEY_PURCHASE = longPreferencesKey("KEY_PURCHASE")
        private val KEY_EXPIRATION = longPreferencesKey("KEY_EXPIRATION")
    }

    val isPremiumFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        val purchase = prefs[KEY_PURCHASE] ?: 0L
        val expiration = prefs[KEY_EXPIRATION] ?: 0L
        val isPremium = prefs[KEY_SUB] ?: false

        val now = System.currentTimeMillis()
        val betweenDates = now in purchase..expiration

        isPremium && betweenDates
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching { refreshPremiumState() }
        }
    }

    suspend fun refreshPremiumState() {
        try {
            val purchases = purchaseInteractor
                .getPurchases(productType = ProductType.SUBSCRIPTION)
                .await()

            val active = purchases
                .filterIsInstance<SubscriptionPurchase>()
                .find { it.status == SubscriptionPurchaseStatus.ACTIVE }


            if (active != null) {
                val purchaseTime = active.purchaseTime?.time ?: 0L
                val expiration = active.expirationDate.time
                savePremiumState(true, purchaseTime, expiration)
            } else {
                savePremiumState(false, 0L, 0L)
            }
        } catch (_: Exception) {
        }
    }

    suspend fun getExpirationDate(): Long? {
        val prefs = context.dataStore.data.first()
        return prefs[KEY_EXPIRATION]
    }

    private suspend fun savePremiumState(isPremium: Boolean, purchaseTime: Long, expiration: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SUB] = isPremium
            prefs[KEY_PURCHASE] = purchaseTime
            prefs[KEY_EXPIRATION] = expiration
        }
    }
}