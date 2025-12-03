package danilfedorov.feature_fruit_album.internal.mvi

sealed class Action {
    object NavHome: Action()
    object ShowAd: Action()
    class ToggleEnabled(val index: Int): Action()
    object HideUnlockFruitSheet: Action()
}