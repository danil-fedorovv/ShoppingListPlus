@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.shared_shoppinglist.presentation.compose.sheet.share

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import danilfedorov.common.compose.AppModalBottomSheet

@Composable
fun ShareListSheet(
    shareText: () -> Unit,
    liveEditLink: () -> Unit,
    dismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = dismissRequest,
        backgroundColor = colorScheme.background
    ) {
        ShareListSheetContent(
            shareText = shareText,
            liveEditLink = liveEditLink,
            dismissRequest = dismissRequest
        )
    }
}