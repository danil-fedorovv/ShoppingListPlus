package danilfedorov.feature_add_product.internal.di

import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.product.DeleteProductsUseCase
import danilfedorov.domain_core.usecase.product.ObserveProductListByListIdUseCase
import danilfedorov.domain_core.usecase.suggestion.InsertProductsFromNamesUseCase
import danilfedorov.domain_core.usecase.suggestion.InsertOrIncrementSuggestionUseCase
import danilfedorov.domain_core.usecase.suggestion.SearchProductSuggestionsUseCase
import danilfedorov.feature_add_product.api.AddProductDeps
import danilfedorov.feature_add_product.internal.presenter.viewmodel.AddProductViewModel

@FeatureScope
@Component(dependencies = [AddProductDeps::class])
interface AddProductComponent {

    val addProductViewModelFactory: AddProductViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(deps: AddProductDeps): AddProductComponent
    }
}