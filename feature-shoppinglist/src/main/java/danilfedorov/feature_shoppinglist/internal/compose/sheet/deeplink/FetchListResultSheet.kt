@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_shoppinglist.internal.compose.sheet.deeplink

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.feature_shoppinglist.R
import danilfedorov.shared_shoppinglist.presentation.compose.sheet.push_list.DeepLinkSheetContent

@Composable
fun FetchListResultSheet(
    fetchListSheetState: FetchListSheetState,
    onClickJoin: (String) -> Unit,
    onClickAddList: () -> Unit,
    onDismissRequest: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        backgroundColor = colorScheme.background
    ) {
        when (fetchListSheetState) {
            is FetchListSheetState.Invite -> {
                DeepLinkSheetContent(
                    imageResId = danilfedorov.common.R.drawable.img_coop,
                    title = stringResource(R.string.invite_title),
                    description = stringResource(R.string.invite_description),
                    listName = fetchListSheetState.listName,
                    isJoining = fetchListSheetState.isJoining,
                    buttonText = stringResource(R.string.accept_button),
                    buttongColor = colorScheme.primary,
                    onClick = { onClickJoin(fetchListSheetState.remoteListId) },
                )
            }

            is FetchListSheetState.Error -> {
                DeepLinkSheetContent(
                    iconResId = danilfedorov.common.R.drawable.ic_warning,
                    title = stringResource(R.string.error_title),
                    description = stringResource(R.string.error_subtitle),
                    buttonText = stringResource(R.string.retry_button),
                    onClick = { onClickJoin(fetchListSheetState.remoteListId) }
                )
            }

            is FetchListSheetState.Timeout -> {
                DeepLinkSheetContent(
                    iconResId = danilfedorov.common.R.drawable.ic_internet_error,
                    title = stringResource(R.string.timeout_title),
                    description = stringResource(R.string.timeout_subtitle),
                    buttonText = stringResource(R.string.retry_button),
                    onClick = { onClickJoin(fetchListSheetState.remoteListId) }
                )
            }

            FetchListSheetState.NotFound -> {
                DeepLinkSheetContent(
                    iconResId = R.drawable.ic_bad_link,
                    title = stringResource(R.string.list_not_found_title),
                    description = stringResource(R.string.list_not_found_subtitle),
                    buttonText = stringResource(R.string.list_not_found_button),
                    onClick = { onDismissRequest() }
                )
            }

            is FetchListSheetState.CopyInvite -> {
                DeepLinkSheetContent(
                    imageResId = danilfedorov.common.R.drawable.img_coop,
                    title = stringResource(R.string.add_list_title),
                    listName = fetchListSheetState.listName,
                    buttonText = stringResource(R.string.add_list_button),
                    buttongColor = colorScheme.primary,
                    onClick = {
                        onClickAddList()
                    }
                )
            }

            else -> Unit
        }
    }

}