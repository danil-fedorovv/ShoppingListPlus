package danilfedorov.feature_productlist.internal.compose.sheet.edit_product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AppTextField
import danilfedorov.productlist.R


@Composable
fun EditAmountAndUnitRow(
    draftAmount: String,
    draftUnit: String,
    onChangeAmount: (String) -> Unit,
    onChangeUnit: (String) -> Unit,
    incAmount: () -> Unit,
    decAmount: () -> Unit,
) {
    val height = 56.dp

    Row(
        Modifier
            .fillMaxWidth()
            .height(height),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppTextField(
            value = draftAmount,
            onValueChange = { onChangeAmount(it) },
            label = stringResource(R.string.amount),
            modifier = Modifier.weight(2f),
            maxSymbols = 7,
            keyboardType = KeyboardType.Number,
            contentPadding = TextFieldDefaults.contentPaddingWithLabel()
        )
        AppTextField(
            value = draftUnit,
            onValueChange = { onChangeUnit(it) },
            label = stringResource(R.string.units),
            modifier = Modifier.weight(2f),
            maxSymbols = 10,
            contentPadding = TextFieldDefaults.contentPaddingWithLabel()
        )
        Row(
            Modifier.height(height),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIconButton(
                painterResource = painterResource(R.drawable.ic_minus),
                contentDescription = stringResource(R.string.inc_amount),
                onClick = { decAmount() },
                enable = (draftAmount.toIntOrNull() ?: 0) > 0
            )
            CircleIconButton(
                painterResource = painterResource(danilfedorov.common.R.drawable.ic_plus),
                contentDescription = stringResource(R.string.dec_amount),
                onClick = { incAmount() }
            )
        }
    }
}