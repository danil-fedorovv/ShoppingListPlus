@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_productlist.internal.compose.sheet.options

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import danilfedorov.common.compose.AppModalBottomSheet

@Composable
fun OptionsSheet(
    search: () -> Unit,
    shareListAsText: () -> Unit,
    shareLiveEditLink: () -> Unit,
    renameList: () -> Unit,
    selectAll: () -> Unit,
    clearSelection: () -> Unit,
    deleteAllSelected: () -> Unit,
    onDismissRequest: () -> Unit,
    isCheckedSort: Boolean,
    switchSort: (Boolean) -> Unit
    ) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        backgroundColor = colorScheme.background
    ) {
        OptionsSheetContent(
            search = search,
            shareListAsText = shareListAsText,
            shareLiveEditLink = shareLiveEditLink,
            renameList = renameList,
            selectAll = selectAll,
            clearSelection = clearSelection,
            deleteAllSelected = deleteAllSelected,
            isCheckedSort = isCheckedSort,
            switchSort = switchSort
        )
    }
}