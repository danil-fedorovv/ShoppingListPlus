@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.shared_shoppinglist.presentation.compose.sheet.push_list

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.shared_shoppinglist.R


@Composable
fun PushListResultSheet(
    pushListSheetState: PushListSheetState,
    onClickRetry: (
        context: Context,
        shoppingList: ShoppingList,
        products: List<Product>,
    ) -> Unit,
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
        when (pushListSheetState) {
            is PushListSheetState.Error -> {
                DeepLinkSheetContent(
                    iconResId = danilfedorov.common.R.drawable.ic_warning,
                    title = stringResource(R.string.error_title),
                    description = stringResource(R.string.error_subtitle),
                    buttonText = stringResource(R.string.retry_button),
                    onClick = { onClickRetry(pushListSheetState.context, pushListSheetState.list, pushListSheetState.products) }
                )
            }

            is PushListSheetState.Timeout -> {
                DeepLinkSheetContent(
                    iconResId = danilfedorov.common.R.drawable.ic_internet_error,
                    title = stringResource(R.string.timeout_title),
                    description = stringResource(R.string.timeout_subtitle),
                    buttonText = stringResource(R.string.retry_button),
                    onClick = { onClickRetry(pushListSheetState.context, pushListSheetState.list, pushListSheetState.products) }
                )
            }

            else -> Unit
        }
    }

}