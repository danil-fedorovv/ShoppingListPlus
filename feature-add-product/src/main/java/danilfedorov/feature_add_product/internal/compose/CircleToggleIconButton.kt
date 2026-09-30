package danilfedorov.feature_add_product.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.outline
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun CircleToggleIconButton(
    progress: Float,
    toggleSuggestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = lerp(outline, colorScheme.primary, progress)
    val rotation = progress * 180f

    Box(
        modifier = modifier
            .size(24.dp)
            .background(background, CircleShape)
            .noRippleClickable { toggleSuggestion() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(danilfedorov.common.R.drawable.ic_plus2),
            contentDescription = null,
            tint = colorScheme.background,
            modifier = Modifier
                .size(12.dp)
                .graphicsLayer { rotationZ = rotation }
        )
    }
}