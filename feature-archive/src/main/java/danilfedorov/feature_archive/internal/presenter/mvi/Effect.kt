package danilfedorov.feature_archive.internal.presenter.mvi

sealed class Effect {
    class ShowSnackbarDelete(val message: String): Effect()
}