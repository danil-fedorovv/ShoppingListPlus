package danilfedorov.feature_shoppinglist.internal.compose.sheet.deeplink

import danilfedorov.domain_core.model.Product

sealed class FetchListSheetState {
    object Hidden : FetchListSheetState()
    object Loading : FetchListSheetState()
    data class Invite(
        val listName: String,
        val remoteListId: String,
        val isJoining: Boolean = false,
    ) : FetchListSheetState()

    data class Timeout(val remoteListId: String) : FetchListSheetState()
    object NotFound : FetchListSheetState()
    data class Error(val remoteListId: String) : FetchListSheetState()
    data class CopyInvite(val listName: String, val products: List<Product>): FetchListSheetState()
}