package danilfedorov.feature_shoppinglist.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_shoppinglist.api.ShoppingListDeps
import danilfedorov.feature_shoppinglist.internal.di.DaggerShoppingListComponent

internal class ShoppingListViewModelFactory(
    val navArchive: () -> Unit,
    val navSettings: () -> Unit,
    val navProduct: (id: Long) -> Unit,
    val deps: ShoppingListDeps
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val component = DaggerShoppingListComponent.factory().create(deps)

        return ShoppingListViewModel(
            navArchive,
            navSettings,
            navProduct,
            component.getListsUseCase,
            component.createListUseCase,
            component.archiveShoppingListsUseCase,
            component.renameShoppingListUseCase,
            component.getSelectedProductCountUseCase,
            component.getTotalProductCountUseCase,
            component.getProductListByListIdUseCase,
            component.createProductUseCase,
            component.res
        ) as T
    }
}