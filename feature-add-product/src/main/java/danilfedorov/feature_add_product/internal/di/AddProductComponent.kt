package danilfedorov.feature_add_product.internal.di

import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.premium.PremiumStatusFlowUseCase
import danilfedorov.domain_core.usecase.product.DeleteProductsUseCase
import danilfedorov.domain_core.usecase.product.ObserveProductListByListIdUseCase
import danilfedorov.domain_core.usecase.suggestion.InsertProductsFromNamesUseCase
import danilfedorov.domain_core.usecase.suggestion.InsertOrIncrementSuggestionUseCase
import danilfedorov.domain_core.usecase.suggestion.SearchProductSuggestionsUseCase
import danilfedorov.feature_add_product.api.AddProductDeps

@FeatureScope
@Component(dependencies = [AddProductDeps::class], modules = [UseCaseModule::class])
interface AddProductComponent {

    val searchProductSuggestionsUseCase: SearchProductSuggestionsUseCase
    val insertProductsFromNamesUseCase: InsertProductsFromNamesUseCase
    val insertOrIncrementSuggestionUseCase: InsertOrIncrementSuggestionUseCase
    val observeProductListByListIdUseCase: ObserveProductListByListIdUseCase
    val deleteProductsUseCase: DeleteProductsUseCase
    val premiumStatusFlowUseCase: PremiumStatusFlowUseCase

    @Component.Factory
    interface Factory {
        fun create(deps: AddProductDeps): AddProductComponent
    }
}