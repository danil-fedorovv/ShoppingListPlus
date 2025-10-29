package danilfedorov.common.extension.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.elevatedCard(): Modifier {
    val shape = MaterialTheme.shapes.medium
    val colorScheme = MaterialTheme.colorScheme

    return this
        .dropShadow(
            shape = shape,
            shadow = Shadow(
                radius = 4.dp,
                spread = 0.5.dp,
                color = colorScheme.onSurface.copy(alpha = 0.25f)
            )
        )
        .background(
            color = colorScheme.onBackground,
            shape = shape
        )
        .border(
            width = 1.dp,
            color = colorScheme.surface,
            shape = shape
        )
}