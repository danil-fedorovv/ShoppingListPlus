package danilfedorov.feature_archive.internal.presenter.mvi

import danilfedorov.domain_core.model.ShoppingList

sealed class Action {
    class NavHome(): Action()
    class DeleteArchive(val archive: ShoppingList): Action()
    class RestoreState(): Action()
    class ConfirmDeleteArchive(): Action()
    class RestoreArchive(val archive: ShoppingList): Action()
    class ShowProductsSheet(val archive: ShoppingList): Action()
    class DismissProductsSheet(): Action()
    class DeleteAll: Action()

}