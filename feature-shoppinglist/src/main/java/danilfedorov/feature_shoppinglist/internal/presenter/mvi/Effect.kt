package danilfedorov.feature_shoppinglist.internal.presenter.mvi

sealed class Effect {
    class ShowSnackbarDelete(val count: Int): Effect()
    class ShowSnackbarInfo(val message: String): Effect()
}