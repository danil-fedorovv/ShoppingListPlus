package danilfedorov.feature_settings.internal.presenter.mvi

sealed class Effect {
    object FailedToLoadAd : Effect()
    object NavRateUs : Effect()
    object NavContactUs: Effect()
}