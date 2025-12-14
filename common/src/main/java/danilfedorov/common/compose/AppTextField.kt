package danilfedorov.common.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxSymbols: Int = Int.MAX_VALUE,
    onDone: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    textStyle: TextStyle = typography.titleMedium,
    containerColor: Color = colorScheme.surface,
    focusable: Boolean = true,
    focusRequester: FocusRequester = remember { FocusRequester() }
) {


    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = if (isFocused) colorScheme.primary else colorScheme.surface

    val colors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedLabelColor = colorScheme.primary,
        unfocusedLabelColor = colorScheme.onSurfaceVariant,
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        focusedTextColor = colorScheme.secondary,
        unfocusedTextColor = colorScheme.secondary
    )

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                val text = if (newValue.length <= maxSymbols) newValue else newValue.take(maxSymbols)
                onValueChange(text)
            },
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .focusable(focusable)
                .dropShadow(
                    shape = shapes.medium,
                    shadow = Shadow(
                        radius = 5.dp,
                        spread = 1.dp,
                        color = colorScheme.onSurface
                    )
                )
                .border(width = 1.dp, color = borderColor, shape = shapes.medium)
            ,
            enabled = focusable,
            readOnly = !focusable,
            textStyle = textStyle.copy(color = colorScheme.secondary),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone?.invoke()
                }
            ),
            singleLine = true,
            maxLines = 1,
            minLines = 1,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(value = colorScheme.primary),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    isError = false,
                    label = label?.let { text ->
                        {
                            Text(
                                text = text,
                                style = typography.titleSmall,
                                overflow = TextOverflow.Ellipsis,
                                color = colorScheme.outline
                            )
                        }
                    },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = null,
                    suffix = null,
                    supportingText = null,
                    colors = colors,
                    contentPadding = contentPadding,
                    shape = shapes.medium
                )
            }
        )
    }
}
