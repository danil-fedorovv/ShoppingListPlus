@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.R
import danilfedorov.common.core_res.fruitImages
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun EditShoppingListNameSheet(
    title: String = stringResource(R.string.rename_list),
    buttonText: String = stringResource(R.string.save),
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
    selectAllOnFocus: Boolean = true,
    autoFocus: Boolean = true,
    placeholderText: String = ""
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        backgroundColor = colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(fruitImages.random()),
                contentDescription = null,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = typography.titleLarge,
                color = colorScheme.secondary
            )
            Spacer(Modifier.height(8.dp))

            AppTextField(
                modifier = Modifier.padding(vertical = 16.dp),
                value = value,
                onValueChange = { onValueChange(it) },
                textStyle = typography.titleLarge,
                containerColor = colorScheme.onBackground,
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                selectAllOnFocus = selectAllOnFocus,
                autoFocus = autoFocus,
                placeholder = if(placeholderText.isNotEmpty()) {
                    {
                        Text(
                            text = placeholderText,
                            color = colorScheme.outline,
                            style = typography.titleLarge
                        )
                    }
                } else null
            )
            Spacer(Modifier.height(16.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorScheme.primary, shape = shapes.extraLarge)
                    .padding(vertical = 16.dp)
                    .noRippleClickable {
                        onClick()
                        onDismissRequest()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buttonText,
                    style = typography.titleMedium,
                    color = colorScheme.background
                )
            }
        }
    }
}