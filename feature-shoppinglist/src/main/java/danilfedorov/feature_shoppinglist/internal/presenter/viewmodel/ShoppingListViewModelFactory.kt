package danilfedorov.feature_shoppinglist.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.domain_core.usecase.shoppinglist.ObserveArchivedShoppingListsUseCase
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
            navArchive = navArchive,
            navSettings = navSettings,
            navProducts = navProduct,
            observeShoppingListsUseCase = component.observeShoppingListsUseCase,
            createListUseCase = component.createListUseCase,
            archiveShoppingListsUseCase = component.archiveShoppingListsUseCase,
            renameShoppingListUseCase = component.renameShoppingListUseCase,
            getSelectedProductCountUseCase = component.getSelectedProductCountUseCase,
            getTotalProductCountUseCase = component.getTotalProductCountUseCase,
            getProductListByListIdUseCase = component.getProductListByListIdUseCase,
            fetchListUseCase = component.fetchListUseCase,
            insertProductsUseCase = component.insertProductsUseCase,
            pushListUseCase = component.pushListUseCase,
            getRemoteShoppingListNameUseCase = component.getRemoteShoppingListNameUseCase,
            observeRemoteListNameUseCase = component.observeRemoteListNameUseCase,
            observeArchivedShoppingLists = component.observeArchivedShoppingListsUseCase,
            res = component.res
        ) as T
    }
}