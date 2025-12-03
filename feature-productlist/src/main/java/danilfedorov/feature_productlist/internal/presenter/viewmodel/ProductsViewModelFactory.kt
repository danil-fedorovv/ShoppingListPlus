package danilfedorov.feature_productlist.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_productlist.api.ProductListDeps
import danilfedorov.feature_productlist.internal.di.DaggerProductListComponent

internal class ProductsViewModelFactory(
    private val shoppingListId: Long,
    private val onNavigateHome: () -> Unit,
    private val deps: ProductListDeps
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val component = DaggerProductListComponent.factory().create(deps)

        return ProductsViewModel(
            shoppingListId = shoppingListId,
            onNavigateHome = onNavigateHome,
            res = component.resources,
            renameShoppingListUseCase = component.renameShoppingListUseCase,
            observeProductListByListIdUseCase = component.observeProductListByListIdUseCase,
            insertProductUseCase = component.insertProductUseCase,
            updateProductSelectionUseCase = component.updateProductUseCase,
            selectAllProductsUseCase = component.selectAllProductsUseCase,
            unselectAllProductsUseCase = component.unselectAllProductsUseCase,
            updateProductCardUseCase = component.updateProductCardUseCase,
            deleteProductsUseCase = component.deleteProductsUseCase,
            observeShoppingListByIdUseCase = component.observeShoppingListByIdUseCase,
            changeSortByAlphabetUseCase = component.changeSortByAlphabetUseCase,
            getShoppingListProgressUseCase = component.getShoppingListProgressUseCase,
            createListUseCase = component.createListUseCase,
            pushListUseCase = component.pushListUseCase,
            getShoppingListsUseCase = component.getShoppingListsUseCase,
            observeRemoteProductChangesUseCase = component.observeRemoteProductChangesUseCase,
            observeConnectionStatusUseCase = component.observeConnectionStatusUseCase,
            observeRemoteListNameUseCase = component.observeRemoteListNameUseCase,
            getRandomActiveUnlockedFruitUseCase = component.getRandomActiveUnlockedFruitUseCase
        ) as T

    }
}