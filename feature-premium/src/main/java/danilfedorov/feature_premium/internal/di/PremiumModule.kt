package danilfedorov.feature_premium.internal.di

import dagger.Module
import dagger.Provides
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.PremiumRepository
import danilfedorov.domain_core.usecase.premium.GetPremiumExpirationUseCase
import danilfedorov.domain_core.usecase.premium.PremiumStatusFlowUseCase
import danilfedorov.domain_core.usecase.premium.RefreshPremiumStateUseCase
import ru.rustore.sdk.pay.PurchaseInteractor
import ru.rustore.sdk.pay.RuStorePayClient

@Module
object PremiumModule {

    @Provides
    @FeatureScope
    fun providePurchaseInteractor(): PurchaseInteractor {
        return RuStorePayClient.instance.getPurchaseInteractor()
    }

    @Provides
    @FeatureScope
    fun provideGetPremiumExpirationUseCase(repository: PremiumRepository): GetPremiumExpirationUseCase {
        return GetPremiumExpirationUseCase(repository)
    }

    @Provides
    @FeatureScope
    fun providePremiumStatusFlowUseCase(repository: PremiumRepository): PremiumStatusFlowUseCase {
        return PremiumStatusFlowUseCase(repository)
    }

    @Provides
    @FeatureScope
    fun provideRefreshPremiumStateUseCase(repository: PremiumRepository): RefreshPremiumStateUseCase {
        return RefreshPremiumStateUseCase(repository)
    }
}