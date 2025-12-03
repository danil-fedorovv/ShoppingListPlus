package danilfedorov.common.extension.modifier

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.elevatedCard(
    shadowColor: Color = colorScheme.surface
): Modifier {
    val shape = MaterialTheme.shapes.medium

    return this
        .dropShadow(
            shape = shape,
            shadow = Shadow(
                radius = 4.dp,
                spread = 0.5.dp,
                color = shadowColor
            )
        )
        .background(
            color = colorScheme.onBackground,
            shape = shape
        )
}