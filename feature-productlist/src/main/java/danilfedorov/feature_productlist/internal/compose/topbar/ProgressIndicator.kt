package danilfedorov.feature_productlist.internal.compose.topbar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(targetProgress: Float) {
    var isFirstLaunch by remember { mutableStateOf(true) }
    val animaDuration = if (isFirstLaunch) 0 else 300

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(animaDuration),
        label = "progress_indicator_label",
        finishedListener = { isFirstLaunch = false }
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        color = colorScheme.primary,
        trackColor = colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp),
        drawStopIndicator = {},
        gapSize = 0.dp,
        strokeCap = StrokeCap.Square
    )

}