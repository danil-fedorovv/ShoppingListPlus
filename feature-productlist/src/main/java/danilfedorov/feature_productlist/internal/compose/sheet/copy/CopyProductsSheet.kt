@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_productlist.internal.compose.sheet.copy

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.domain_core.model.ShoppingList

@Composable
fun CopyProductsSheet(
    shoppingListProgress: Map<ShoppingList, String>,
    copyItems: (Long) -> Unit,
    createNewListAndCopy: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        backgroundColor = colorScheme.background
    ) {
        CopyProductsSheetContent(
            shoppingListProgress = shoppingListProgress,
            copyItems = copyItems,
            createNewListAndCopy = createNewListAndCopy
        )
    }
}