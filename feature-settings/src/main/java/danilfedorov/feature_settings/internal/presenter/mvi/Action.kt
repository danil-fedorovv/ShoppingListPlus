package danilfedorov.feature_settings.internal.presenter.mvi

sealed class Action {
    object NavBack : Action()
    object NavFruitCollection : Action()
    object NavPremium : Action()

    object RateUs : Action()
    object ContactUs : Action()

    class SetDarkTheme(val darkTheme: Boolean) : Action()
    class SaveColor(val colorIndex: Int) : Action()
    object ShowChangeColorSheet: Action()
    object HideChangeColorSheet: Action()
}