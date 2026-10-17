package danilfedorov.feature_shoppinglist.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.domain_core.usecase.shoppinglist.ObserveArchivedShoppingListsUseCase
import danilfedorov.feature_shoppinglist.api.ShoppingListDeps
import danilfedorov.feature_shoppinglist.internal.di.DaggerShoppingListComponent

internal class ShoppingListViewModelFactory(
    private val navArchive: () -> Unit,
    private val navSettings: () -> Unit,
    private val navProduct: (id: Long) -> Unit,
    private val deps: ShoppingListDeps,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = DaggerShoppingListComponent.factory().create(deps)

        @Suppress("UNCHECKED_CAST")
        return component.shoppingListViewModelFactory.create(
            navArchive = navArchive,
            navSettings = navSettings,
            navProducts = navProduct,
        ) as T
    }
}