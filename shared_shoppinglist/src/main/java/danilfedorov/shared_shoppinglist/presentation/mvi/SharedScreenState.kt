package danilfedorov.shared_shoppinglist.presentation.mvi

import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.shared_shoppinglist.presentation.compose.sheet.push_list.PushListSheetState

data class SharedScreenState(
    val isShareListSheetVisible: Boolean = false,
    val isLinkLoading: Boolean = false,
    val pushListSheetState: PushListSheetState = PushListSheetState.Hidden,
    val tempListName: String = "",
    val renamingList: ShoppingList? = null,
    val renameSheetFruitResId: Int = danilfedorov.common.R.drawable.grape
)