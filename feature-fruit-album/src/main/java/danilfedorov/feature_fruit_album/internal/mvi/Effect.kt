package danilfedorov.feature_fruit_album.internal.mvi

sealed class Effect {
    object FailedToLoadAd : Effect()
}