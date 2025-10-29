package danilfedorov.feature_productlist.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_productlist.api.ProductListDeps
import danilfedorov.feature_productlist.internal.di.DaggerProductListComponent

internal class ProductListViewModelFactory(
    private val shoppingListId: Long,
    private val onNavigateHome: () -> Unit,
    private val deps: ProductListDeps
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val component = DaggerProductListComponent.factory().create(deps)

        return ProductListViewModel(
            shoppingListId,
            onNavigateHome,
            component.resources,
            component.renameShoppingListUseCase,
            component.getProductListByListIdUseCase,
            component.createProductUseCase,
            component.updateProductUseCase,
            component.selectAllProductsUseCase,
            component.unselectAllProductsUseCase,
            component.updateProductCardUseCase,
            component.deleteProductsUseCase,
            component.getShoppingListByIdUseCase,
            component.changeSortByAlphabetUseCase,
            component.getListsUseCase,
            component.getShoppingListProgressUseCase,
            component.createListUseCase
        ) as T

    }
}