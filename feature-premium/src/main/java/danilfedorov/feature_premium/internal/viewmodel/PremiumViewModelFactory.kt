package danilfedorov.feature_premium.internal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_premium.api.PremiumDeps
import danilfedorov.feature_premium.internal.di.DaggerPremiumComponent

class PremiumViewModelFactory(
    private val deps: PremiumDeps,
    private val navHome: () -> Unit
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val component = DaggerPremiumComponent.factory().create(deps)

        return PremiumViewModel(
            navHome,
            component.purchaseInteractor,
            component.getPremiumExpirationUseCase,
            component.premiumStatusFlowUseCase,
            component.refreshPremiumStateUseCase) as T
    }
}