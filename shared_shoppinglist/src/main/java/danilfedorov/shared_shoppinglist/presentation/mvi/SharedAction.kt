package danilfedorov.shared_shoppinglist.presentation.mvi

import android.content.Context
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList

sealed class SharedAction {
    class ShareText(
        val context: Context,
        val listName: String = "",
        val shoppingList: ShoppingList? = null,
        val products: List<Product> = emptyList(),
    ) : SharedAction()

    class ShareLiveEditLink(
        val context: Context,
        val shoppingList: ShoppingList? = null,
        val products: List<Product> = emptyList(),
    ) : SharedAction()

    object HidePushListSheet : SharedAction()
    class ChangeTempListName(val newValue: String) : SharedAction()
    object SaveListName : SharedAction()
    class ShowRenameListSheet(val shoppingList: ShoppingList) : SharedAction()
    object HideRenameListSheet : SharedAction()
}