package danilfedorov.feature_productlist.internal.compose.item

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

val left = "Left"
val center = "Center"
val right = "Right"

@Composable
fun SwipeableLayout(
    state: AnchoredDraggableState<String>,
    activePositions: List<String> = listOf(left, center, right),
    modifier: Modifier = Modifier,
    onLeftReached: () -> Unit = {},
    onRightReached: () -> Unit = {},
    swipeContent: @Composable () -> Unit,  // Контент, который свайпается (твой элемент)
) {
    var elementWidth by remember { mutableFloatStateOf(0f) } // Ширина для расчета прогресса
    var elementHeight by remember { mutableFloatStateOf(0f) } // Высота для фонов

    Box(modifier) {
        // Левый фон (красный с иконкой delete)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(elementHeight.dp)
                .background(Color.Red.copy(alpha = if (state.offset < 0) abs(state.offset / elementWidth) else 0f)),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.offset(x = (state.offset / 2).dp) // Динамический offset для приближения иконки
            )
        }

        // Правый фон (зеленый с иконкой check)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(elementHeight.dp)
                .background(Color.Green.copy(alpha = if (state.offset > 0) abs(state.offset / elementWidth) else 0f)),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Confirm",
                tint = Color.White,
                modifier = Modifier.offset(x = (state.offset / 2).dp) // Динамический offset для приближения иконки
            )
        }
        val density = LocalDensity.current.density

        // Сам свайпаемый элемент
        Box(
            Modifier
                .anchoredDraggable(state, Orientation.Horizontal)
                .offset { IntOffset(x = state.requireOffset().roundToInt(), y = 0) }
                .onSizeChanged { measuredSize ->
                    elementWidth = measuredSize.width.toFloat()
                    elementHeight = measuredSize.height.toFloat() / density // Конвертируем в dp
                    state.updateAnchors(
                        DraggableAnchors {
                            if (left in activePositions) {
                                left at -measuredSize.width.toFloat()
                            }
                            if (center in activePositions) {
                                center at 0f
                            }
                            if (right in activePositions) {
                                right at measuredSize.width.toFloat()
                            }
                        }
                    )
                }
        ) {
            swipeContent()
        }
    }

    // Взаимодействие: Обработка достижения анчоров
    LaunchedEffect(state.currentValue) {
        when (state.currentValue) {
            left -> onLeftReached()
            right -> onRightReached()
            // Center — ничего не делаем
        }
    }
}

// Пример использования
@Composable
@Preview
fun DemoSwipeableElement() {
    val state = rememberSaveable(saver = AnchoredDraggableState.Saver()) {
        AnchoredDraggableState(initialValue = center)
    }
    val activePositions = remember { mutableStateListOf(left, center, right) }

    SwipeableLayout(
        state = state,
        activePositions = activePositions,
        onLeftReached = { println("Достигнут левый анкор! Можно удалить элемент или что-то.") },
        onRightReached = { println("Достигнут правый анкор! Можно архивировать или другое действие.") },
        swipeContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.Blue),
                contentAlignment = Alignment.Center
            ) {
                Text("Свайпай меня влево или вправо", color = Color.White)
            }
        }
    )
}