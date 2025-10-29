package danilfedorov.feature_productlist.internal.compose.sheet.edit_product

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import danilfedorov.common.compose.AppModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductBottomSheet(
    draftName: String,
    draftAmount: String,
    draftUnit: String,
    onChangeName: (String) -> Unit,
    onChangeAmount: (String) -> Unit,
    onChangeUnit: (String) -> Unit,
    incAmount: () -> Unit,
    decAmount: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        EditProductBottomSheetContent(
            draftName = draftName,
            draftAmount = draftAmount,
            draftUnit = draftUnit,
            onChangeName = onChangeName,
            onChangeAmount = onChangeAmount,
            onChangeUnit = onChangeUnit,
            incAmount = incAmount,
            decAmount = decAmount,
            onDismiss = onDismiss
        )
    }
}