package danilfedorov.feature_premium.internal.enum

enum class PremiumType(val productId: String, val price: String) {
    MONTHLY("slp_premium_monthly", "29 ₽"),
    HALF_YEAR("slp_premium_half_yearly", "129 ₽"),
    YEARLY("slp_premium_yearly", "229 ₽");
}