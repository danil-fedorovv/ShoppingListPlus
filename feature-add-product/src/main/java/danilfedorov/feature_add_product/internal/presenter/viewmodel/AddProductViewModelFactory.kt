package danilfedorov.feature_add_product.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_add_product.api.AddProductDeps
import danilfedorov.feature_add_product.internal.di.DaggerAddProductComponent

class AddProductViewModelFactory(
    val shoppingListId: Long,
    val remoteListId: String?,
    val navBack: (Long, Int?) -> Unit,
    val deps: AddProductDeps,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val component = DaggerAddProductComponent.factory().create(deps)

        return AddProductViewModel(
            shoppingListId,
            remoteListId,
            navBack,
            component.searchProductSuggestionsUseCase,
            component.insertProductsFromNamesUseCase,
            component.insertOrIncrementSuggestionUseCase,
            component.observeProductListByListIdUseCase,
            component.deleteProductsUseCase,
            component.premiumStatusFlowUseCase
        ) as T
    }
}