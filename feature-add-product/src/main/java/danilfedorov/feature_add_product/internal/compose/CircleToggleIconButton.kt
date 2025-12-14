package danilfedorov.feature_add_product.internal.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.outline
import danilfedorov.common.extension.modifier.noRippleClickable
import kotlinx.coroutines.NonCancellable.isActive

@Composable
fun CircleToggleIconButton(
    isAdded: Boolean,
    toggleSuggestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background by animateColorAsState(
        targetValue = if (isAdded) colorScheme.primary else outline,
        animationSpec = tween(300)
    )

    val rotation by animateFloatAsState(
        targetValue = if (isAdded) 180f else 0f,
        animationSpec = tween(400, delayMillis = 0)
    )

    Box(
        modifier = modifier
            .size(24.dp)
            .background(background, CircleShape)
            .noRippleClickable {
                toggleSuggestion()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(danilfedorov.common.R.drawable.ic_plus),
            contentDescription = null,
            tint = colorScheme.background,
            modifier = Modifier
                .size(16.dp)
                .graphicsLayer { rotationZ = rotation }
        )
    }
}