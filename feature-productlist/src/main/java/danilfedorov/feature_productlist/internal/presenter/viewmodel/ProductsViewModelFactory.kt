package danilfedorov.feature_productlist.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_productlist.api.ProductListDeps
import danilfedorov.feature_productlist.internal.di.DaggerProductListComponent

internal class ProductsViewModelFactory(
    private val shoppingListId: Long,
    private val addedCount: Int?,
    private val onNavigateHome: () -> Unit,
    private val navAddProduct: (Long, String?) -> Unit,
    private val deps: ProductListDeps,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = DaggerProductListComponent.factory().create(deps)

        @Suppress("UNCHECKED_CAST")
        return component.productsViewModelFactory.create(
            shoppingListId = shoppingListId,
            addedCount = addedCount,
            onNavigateHome = onNavigateHome,
            navAddProduct = navAddProduct,
        ) as T
    }
}