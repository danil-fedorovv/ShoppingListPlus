package danilfedorov.feature_premium.internal.di

import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.premium.GetPremiumExpirationUseCase
import danilfedorov.domain_core.usecase.premium.PremiumStatusFlowUseCase
import danilfedorov.domain_core.usecase.premium.RefreshPremiumStateUseCase
import danilfedorov.feature_premium.api.PremiumDeps
import ru.rustore.sdk.pay.PurchaseInteractor

@FeatureScope
@Component(dependencies = [PremiumDeps::class], modules = [PremiumModule::class])
interface PremiumComponent {

    val purchaseInteractor: PurchaseInteractor
    val getPremiumExpirationUseCase: GetPremiumExpirationUseCase
    val premiumStatusFlowUseCase: PremiumStatusFlowUseCase
    val refreshPremiumStateUseCase: RefreshPremiumStateUseCase
    @Component.Factory
    interface Factory {
        fun create(deps: PremiumDeps): PremiumComponent
    }
}