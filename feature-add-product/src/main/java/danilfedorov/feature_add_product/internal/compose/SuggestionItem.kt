package danilfedorov.feature_add_product.internal.compose

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.noRippleClickable
import kotlinx.coroutines.launch

@Composable
fun SuggestionItem(
    isAdded: Boolean,
    displayName: String,
    toggleSuggestion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val progress = remember { Animatable(if (isAdded) 1f else 0f) }
    var isAnimating by remember { mutableStateOf(false) }

    val onClick: () -> Unit = {
        if (!isAnimating) {
            scope.launch {
                progress.animateTo(
                    targetValue = if (isAdded) 0f else 1f,
                    animationSpec = tween(350)
                )
                toggleSuggestion()
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(colorScheme.background)
            .padding(horizontal = 16.dp)
            .noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = displayName,
            style = typography.titleMedium,
            color = colorScheme.secondary,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )

        CircleToggleIconButton(
            progress = progress.value,
            toggleSuggestion = onClick
        )
    }
}
