package danilfedorov.feature_add_product.internal.presenter.mvi

sealed class Action {
    class OnChangeSearchInput(val input: String): Action()
    object SaveProducts: Action()
    class ToggleSuggestion(val displayName: String): Action()
    class VoiceInputReceived(val voiceInput: String): Action()

}