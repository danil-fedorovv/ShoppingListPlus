package danilfedorov.feature_productlist.internal.presenter.mvi

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList

data class ScreenState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val unselectedProducts: List<Product> = emptyList(),
    val selectedProducts: List<Product> = emptyList(),
    val draftAddProduct: String = "",
    val editingProductId: Long? = null,
    val draftProductName: String = "",
    val draftAmount: String = "",
    val draftUnit: String = "",
    val isSelectedListExpanded: Boolean = false,
    val multiSelectProducts: List<Product> = emptyList(),
    val isOptionsSheetVisible: Boolean = false,
    val isRenameListVisible: Boolean = false,
    val searchMode: Boolean = false,
    val shoppingListName: String = "",
    val draftShoppingListName: String = "",
    val searchValue: String = "",
    val filteredCount: Int = 0,
    val isSortByAlphabet: Boolean = false,
    val isCopySheetVisible: Boolean = false,
    val shoppingListProgress: Map<ShoppingList, String> = emptyMap(),
) {
    fun updateState(products: List<Product>): ScreenState {
        val (selected, unselected) = products.partition { it.isSelected }
        return copy(
            products = products,
            selectedProducts = selected,
            unselectedProducts = unselected
        )
    }
}