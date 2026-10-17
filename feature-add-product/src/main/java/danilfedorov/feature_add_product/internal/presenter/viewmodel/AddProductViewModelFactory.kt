package danilfedorov.feature_add_product.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_add_product.api.AddProductDeps
import danilfedorov.feature_add_product.internal.di.DaggerAddProductComponent

class AddProductViewModelFactory(
    private val shoppingListId: Long,
    private val remoteListId: String?,
    private val navBack: (Long, Int?) -> Unit,
    private val deps: AddProductDeps,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = DaggerAddProductComponent.factory().create(deps)

        @Suppress("UNCHECKED_CAST")
        return component.addProductViewModelFactory.create(
            shoppingListId = shoppingListId,
            remoteListId = remoteListId,
            navBack = navBack,
        ) as T
    }
}