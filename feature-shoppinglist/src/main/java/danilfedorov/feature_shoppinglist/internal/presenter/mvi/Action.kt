package danilfedorov.feature_shoppinglist.internal.presenter.mvi

import android.content.Context
import danilfedorov.domain_core.model.ShoppingList

sealed class Action {
    class NavigateArchive(): Action()
    class NavigateSettings(): Action()
    class NavigateProduct(val id: Long): Action()

    class ShowListOptions(val shoppingList: ShoppingList): Action()
    class HideListOptions(): Action()
    class DeleteList(val list: ShoppingList): Action()
    class ConfirmDeleteList(): Action()
    class ShareList(val id: Long, val context: Context): Action()
    class RenameList(val id: Long): Action()
    class RestoreState(): Action()
    class CreateList(): Action()
    class ShowAddListSheet(): Action()
    class HideAddListSheet(): Action()
    class ChangeDraftListName(val newValue: String): Action()
    class ExtendList(val id: Long?): Action()
    class ShowRenameListSheet(val shoppingList: ShoppingList): Action()
    class HideRenameListSheet(): Action()
    class ChangeRenameList(val newValue: String): Action()
    class CopyList(val shoppingList: ShoppingList): Action()
    class AddMultiSelect(val shoppingList: ShoppingList): Action()
    class RemoveMultiSelect(val shoppingList: ShoppingList): Action()
    class ClearMultiSelect(): Action()
    class MultiDelete(): Action()
    class MultiCopy(): Action()
}