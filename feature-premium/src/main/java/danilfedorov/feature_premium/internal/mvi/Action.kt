package danilfedorov.feature_premium.internal.mvi

import danilfedorov.feature_premium.internal.enum.PremiumType


sealed class Action {
    object NavHome: Action()
    class Subscribe(): Action()
    class SelectType(val type: PremiumType): Action()
    object HideCongratsSheet : Action()
}