package danilfedorov.feature_shoppinglist.internal.compose.sheet.deeplink

import androidx.compose.runtime.Immutable
import danilfedorov.domain_core.model.Product

@Immutable
sealed class FetchListSheetState {
    data object Hidden : FetchListSheetState()
    data object Loading : FetchListSheetState()

    data class Invite(
        val listName: String,
        val remoteListId: String,
        val isJoining: Boolean = false,
    ) : FetchListSheetState()

    data class Timeout(val remoteListId: String) : FetchListSheetState()

    data object NotFound : FetchListSheetState()

    data class Error(val remoteListId: String) : FetchListSheetState()

    @Immutable
    data class CopyInvite(
        val listName: String,
        val products: List<Product>,
    ) : FetchListSheetState()
}