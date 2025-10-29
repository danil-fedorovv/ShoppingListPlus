package danilfedorov.common.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCheckbox(
    isChecked: Boolean,
    modifier: Modifier = Modifier
) {

    val colors = MaterialTheme.colorScheme
    val primary = colors.primary

    val progress by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )


        Canvas(
            modifier = modifier
                .size(24.dp)

        ) {
            val strokeWidth = 2.5.dp.toPx()
            val size = size.minDimension

            if (progress < 0.5f) {
                val circleProgress = (1 - progress * 2).coerceIn(0f, 1f)
                drawArc(
                    color = primary,
                    startAngle = -90f,
                    sweepAngle = 360f * circleProgress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }

            if (progress > 0.5f) {
                val checkProgress = (progress - 0.5f) * 2
                val startX = size * 0.15f
                val startY = size * 0.5f
                val midX = size * 0.45f
                val midY = size * 0.75f
                val endX = size * 0.85f
                val endY = size * 0.25f

                if (checkProgress <= 0.5f) {
                    val lineProgress = checkProgress * 2
                    val x1 = startX + (midX - startX) * lineProgress
                    val y1 = startY + (midY - startY) * lineProgress
                    drawLine(
                        color = primary,
                        start = Offset(startX, startY),
                        end = Offset(x1, y1),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                } else {
                    drawLine(
                        color = primary,
                        start = Offset(startX, startY),
                        end = Offset(midX, midY),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    val lineProgress = (checkProgress - 0.5f) * 2
                    val x2 = midX + (endX - midX) * lineProgress
                    val y2 = midY + (endY - midY) * lineProgress
                    drawLine(
                        color = primary,
                        start = Offset(midX, midY),
                        end = Offset(x2, y2),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }

}