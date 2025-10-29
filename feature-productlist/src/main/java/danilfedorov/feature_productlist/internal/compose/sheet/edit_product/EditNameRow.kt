package danilfedorov.feature_productlist.internal.compose.sheet.edit_product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AppTextField
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun EditNameRow(
    draftName: String,
    onChangeName: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .height(56.dp)
                .noRippleClickable {
                    onDismiss()
                }
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "back",
                tint = colorScheme.outline
            )
        }
        AppTextField(
            value = draftName,
            onValueChange = { onChangeName(it) },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel()

        )
    }
}