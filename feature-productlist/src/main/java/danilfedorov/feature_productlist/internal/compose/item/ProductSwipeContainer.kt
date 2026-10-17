package danilfedorov.feature_productlist.internal.compose.item

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import danilfedorov.domain_core.model.Product
import danilfedorov.productlist.R
import kotlin.math.abs
import kotlin.math.roundToInt

private const val DISMISS_THRESHOLD_FRACTION = 0.28f
private const val FLING_VELOCITY = 900f

@Composable
fun ProductSwipeContainer(
    modifier: Modifier = Modifier,
    item: Product,
    onDelete: (Product) -> Unit,
    onSelect: (Product) -> Unit,
    content: @Composable (Product) -> Unit
) {
    var containerWidthPx by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var dismissed by remember { mutableStateOf(false) }

    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 220),
        finishedListener = { value ->
            if (dismissed) {
                if (value > 0f) onDelete(item) else onSelect(item)
            }
        },
        label = "swipeOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                val w = size.width.toFloat().coerceAtLeast(1f)
                if (w != containerWidthPx) containerWidthPx = w
            }
    ) {
        SwipeBackground(
            offsetX = animatedOffset,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .pointerInput(item.roomId) {
                    val touchSlop = viewConfiguration.touchSlop

                    awaitPointerEventScope {
                        while (true) {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            val pointerId = down.id
                            val velocityTracker = VelocityTracker()

                            var totalDx = 0f
                            var totalDy = 0f
                            var locked = false
                            var isHorizontal = false
                            var dragging = false

                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes
                                    .firstOrNull { it.id == pointerId } ?: break

                                if (!change.pressed) {
                                    if (dragging) {
                                        val velocityX = velocityTracker.calculateVelocity().x
                                        val threshold = containerWidthPx * DISMISS_THRESHOLD_FRACTION
                                        val flungRight = velocityX > FLING_VELOCITY
                                        val flungLeft = velocityX < -FLING_VELOCITY

                                        when {
                                            flungRight || offsetX >= threshold -> {
                                                dismissed = true
                                                offsetX = containerWidthPx
                                            }
                                            flungLeft || offsetX <= -threshold -> {
                                                dismissed = true
                                                offsetX = -containerWidthPx
                                            }
                                            else -> offsetX = 0f
                                        }
                                    }
                                    break
                                }

                                velocityTracker.addPosition(change.uptimeMillis, change.position)

                                val dx = change.positionChange().x
                                val dy = change.positionChange().y

                                if (!locked) {
                                    totalDx += dx
                                    totalDy += dy
                                    if (abs(totalDx) >= touchSlop || abs(totalDy) >= touchSlop) {
                                        locked = true
                                        isHorizontal = abs(totalDx) > abs(totalDy)
                                    }
                                }

                                if (locked && isHorizontal) {
                                    dragging = true
                                    offsetX += dx
                                    change.consume()
                                } else if (locked && !isHorizontal) {
                                    break
                                }
                            }
                        }
                    }
                }
        ) {
            content(item)
        }
    }
}

@Composable
private fun SwipeBackground(
    offsetX: Float,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val offsetDp = with(density) { offsetX.toDp() }

    val isDelete = offsetX > 0f
    val color = if (isDelete) colorScheme.error else colorScheme.primary

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (offsetX == 0f) Color.Transparent else color),
        contentAlignment = if (isDelete) Alignment.CenterStart else Alignment.CenterEnd
    ) {
        if (isDelete && offsetX != 0f) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = colorScheme.background,
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = offsetDp - 64.dp)
            )
        }
    }
}