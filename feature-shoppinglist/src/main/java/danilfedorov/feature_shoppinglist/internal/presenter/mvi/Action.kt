package danilfedorov.feature_shoppinglist.internal.presenter.mvi

import danilfedorov.common.deeplink.DeepLink
import danilfedorov.domain_core.model.ShoppingList

sealed class Action {
    class NavigateArchive(): Action()
    class NavigateSettings(): Action()
    class NavigateSubscription(): Action()
    class NavigateFruitAlbum(): Action()
    class NavigateProduct(val id: Long): Action()

    class ShowListOptions(val shoppingList: ShoppingList): Action()
    class HideListOptions(): Action()
    class ConfirmDeleteList(): Action()
    class RestoreState(): Action()
    class CreateList(): Action()
    class ShowAddListSheet(): Action()
    class HideAddListSheet(): Action()
    class ChangeDraftListName(val newValue: String): Action()
    class ExtendList(val id: Long?): Action()
    class CopyList(val shoppingList: ShoppingList): Action()
    class AddMultiSelect(val shoppingList: ShoppingList): Action()
    class RemoveMultiSelect(val shoppingList: ShoppingList): Action()
    class ClearMultiSelect(): Action()
    data class DeleteLists(val lists: List<ShoppingList>) : Action()
    class MultiCopy(): Action()
    data class DeleteList(val list: ShoppingList): Action()

    class HandleDeepLink(val deepLink: DeepLink): Action()
    object HideDeepLingSheet: Action()
    class OnClickJoinOrRetry(): Action()
    class OnClickAddUnzipList(): Action()
}