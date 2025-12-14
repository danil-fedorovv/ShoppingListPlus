package danilfedorov.feature_productlist.internal.presenter.mvi

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList

data class ScreenState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val shoppingList: ShoppingList? = null,
    val editingProductId: Long? = null,
    val draftProductName: String = "",
    val draftAmount: String = "",
    val draftUnit: String = "",
    val isSelectedListExpanded: Boolean = false,
    val multiSelectProducts: List<Product> = emptyList(),
    val isOptionsSheetVisible: Boolean = false,
    val searchMode: Boolean = false,
    val shoppingListName: String = "",
    val searchValue: String = "",
    val filteredCount: Int = 0,
    val isSortByAlphabet: Boolean = false,
    val isCopySheetVisible: Boolean = false,
    val shoppingListProgress: Map<ShoppingList, String> = emptyMap(),
    val connectionStatus: Boolean? = null
)