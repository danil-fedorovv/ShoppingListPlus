package danilfedorov.feature_productlist.internal.compose.sheet.edit_product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.ShoppingListThemePreview

@Composable
@Preview(showBackground = true)
fun EditProductBottomSheetContentPreview() {
    ShoppingListThemePreview {
        EditProductBottomSheetContent(
            draftName = "milk",
            draftAmount = "15",
            draftUnit = "pcs",
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}

@Composable
fun EditProductBottomSheetContent(
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

    Column(
        Modifier
            .background(color = colorScheme.onBackground)
            .padding(16.dp)
    ) {

        EditNameRow(
            draftName = draftName,
            onChangeName = onChangeName,
            onDismiss = onDismiss
        )

        Spacer(Modifier.height(32.dp))

        EditAmountAndUnitRow(
            draftAmount = draftAmount,
            draftUnit = draftUnit,
            onChangeAmount = onChangeAmount,
            onChangeUnit = onChangeUnit,
            incAmount = incAmount,
            decAmount = decAmount
        )

        Spacer(Modifier.height(32.dp))

        UnitSelection(draftUnit, onChangeUnit)

    }

}