package danilfedorov.feature_shoppinglist.internal.presenter.mvi

import danilfedorov.domain_core.model.ShoppingList

data class ScreenState(
    val isLoading: Boolean = true,
    val shoppingLists: List<ShoppingList> = emptyList(),
    val progress: Map<Long, Pair<Int, Int>> = emptyMap(),
    val draftListName: String = "",
    val isAddListSheetVisible: Boolean = false,
    val multiSelectLists: List<ShoppingList> = emptyList(),
    val extendedListId: Long? = null,
    val draftRenameList: String = "",
    val renameListId: Long? = null,
    val listOptions: ShoppingList? = null
)