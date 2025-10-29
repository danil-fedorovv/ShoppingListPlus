package danilfedorov.feature_shoppinglist.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
internal fun ActionButton(
    color: Color,
    width: Dp,
    cornerShape: Shape = RectangleShape,
    icon: ImageVector? = null,
    iconRes: Int? = null,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(width)
            .background(color, shape = cornerShape)
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when {
            icon != null -> Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = colorScheme.background
            )

            iconRes != null -> Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                tint = colorScheme.background
            )
        }
    }
}