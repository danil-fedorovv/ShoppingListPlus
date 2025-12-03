package danilfedorov.shared_shoppinglist.presentation.compose.sheet.push_list

import android.content.Context
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList

sealed class PushListSheetState {
    object Hidden : PushListSheetState()
    data class Timeout(
        val list: ShoppingList,
        val products: List<Product>,
        val context: Context,
    ) : PushListSheetState()

    data class Error(
        val list: ShoppingList,
        val products: List<Product>,
        val context: Context,
    ) : PushListSheetState()
}