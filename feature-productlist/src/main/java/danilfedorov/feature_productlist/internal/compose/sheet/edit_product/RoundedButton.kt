package danilfedorov.feature_productlist.internal.compose.sheet.edit_product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.ShoppingListThemePreview
import danilfedorov.common.extension.modifier.noRippleClickable
import io.avdev.productlist.R


@Composable
@Preview
fun CircleIconButtonPreview() {
    ShoppingListThemePreview {
        CircleIconButton(
            painterResource = painterResource(R.drawable.ic_plus),
            onClick = {},
            enable = false
        )
    }
}

@Composable
fun CircleIconButton(
    modifier: Modifier = Modifier,
    painterResource: Painter,
    contentDescription: String? = null,
    enable: Boolean = true,
    onClick: () -> Unit,
    size: Dp = 42.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                if (enable) colorScheme.primary else colorScheme.surface,
                shape = CircleShape
            )
            .noRippleClickable {
                if (enable) onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource,
            contentDescription = contentDescription,
            tint = if (enable) colorScheme.background else colorScheme.onBackground,
            modifier = Modifier.size(16.dp)
        )
    }
}