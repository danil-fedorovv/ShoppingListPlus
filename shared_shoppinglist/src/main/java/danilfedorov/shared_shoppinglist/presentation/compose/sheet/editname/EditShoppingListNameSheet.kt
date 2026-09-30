@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.shared_shoppinglist.presentation.compose.sheet.editname

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.R
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.common.compose.AppTextField
import danilfedorov.common.core_res.outline
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun EditShoppingListNameSheet(
    title: String = stringResource(R.string.rename_list),
    titleImageResId: Int,
    buttonText: String = stringResource(R.string.save),
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
    placeholderText: String = "",
    isAddNewList: Boolean = false
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
                painter = painterResource(titleImageResId),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
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
                placeholder = if (placeholderText.isNotEmpty()) {
                    {
                        Text(
                            text = placeholderText,
                            color = colorScheme.outline,
                            style = typography.titleLarge
                        )
                    }
                } else null,
                trailingIcon = {
                    if (value.isNotEmpty()) {
                        Box(
                            Modifier
                                .size(20.dp)
                                .background(shape = CircleShape, color = outline)
                                .noRippleClickable {
                                    onValueChange("")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_cross),
                                contentDescription = stringResource(R.string.clear_search),
                                tint = colorScheme.surface,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            )

            if (isAddNewList) {
                TemplateChipsRow(onValueChange, value)
                Spacer(Modifier.height(24.dp))
            }



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorScheme.primary, shape = shapes.medium)
                    .padding(vertical = 16.dp)
                    .noRippleClickable {
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buttonText,
                    style = typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}