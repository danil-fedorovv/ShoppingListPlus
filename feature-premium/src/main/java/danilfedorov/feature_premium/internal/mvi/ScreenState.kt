package danilfedorov.feature_premium.internal.mvi

import danilfedorov.feature_premium.internal.enum.PremiumType

data class ScreenState(
    val isPremium: Boolean = false,
    val premiumExpirationDate: String? = null,
    val isCongratsSheetVisible: Boolean = false,
    val subscriptionType: PremiumType = PremiumType.MONTHLY
) {
}