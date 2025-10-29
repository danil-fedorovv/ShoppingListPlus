package danilfedorov.feature_productlist.internal.presenter.mvi

import android.content.Context
import danilfedorov.domain_core.model.Product

sealed class Action {
    class InitEditProduct(val product: Product): Action()
    class EditProduct(val id: Long) : Action()
    class OnChangeProductName(val newValue: String): Action()
    class OnChangeAmount(val newValue: String) : Action()
    class IncAmount(): Action()
    class DecAmount(): Action()
    class OnChangeUnit(val newValue: String) : Action()
    class OnChangeAddProduct(val newValue: String): Action()
    class ToggleSelect(val product: Product) : Action()
    class AddProduct() : Action()
    class DeleteProduct(val product: Product) : Action()
    class RestoreState() : Action()
    class ConfirmDeleteProduct() : Action()
    class ToggleHideSelectedRowExpanded() : Action()
    class NavigateHome(): Action()

    class AddToMultiSelect(val product: Product): Action()
    class DeleteFromMultiSelect(val product: Product): Action()
    class ClearMultiSelect(): Action()
    class MultiDelete(): Action()
    class ConfirmMultiDelete(): Action()

    class ShowOptionsSheet(): Action()
    class DismissOptionsSheet(): Action()
    class SelectAll(): Action()
    class ClearSelection(): Action()
    class DeleteAllSelected(): Action()
    class ShareShoppingList(val context: Context): Action()
    class ShowRenameListSheet(): Action()
    class DismissRenameListSheet(): Action()
    class OnChangeShoppingListName(val newValue: String): Action()
    class SaveNewShoppingListName(): Action()
    class EnableSearchMode(): Action()
    class DisableSearchMode(): Action()
    class OnChangeSearchValue(val newValue: String): Action()

    class SetSortByAlphabet(val checked: Boolean): Action()

    class ShowCopySheet(): Action()
    class DismissCopySheet(): Action()
    class CopyProducts(val shoppingListId: Long): Action()
    class CreateNewListAndCopy(): Action()

}