package danilfedorov.common.extension.modifier

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import danilfedorov.common.compose.NoRippleInteractionSource

fun Modifier.noRippleClickable(
    onClick: () -> Unit
): Modifier {
    return this.clickable(
        indication = null,
        interactionSource = NoRippleInteractionSource
    ) {
        onClick()
    }
}