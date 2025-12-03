package danilfedorov.common.compose


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import kotlin.math.sin


@Composable
fun SnowfallBackground(
    modifier: Modifier = Modifier,
    snowflakeCount: Int = 30  // ← именно 30, как ты хотел
) {
    val density = LocalDensity.current.density
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.toFloat()
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.toFloat()

    Box(modifier = modifier.fillMaxSize()) {
        repeat(snowflakeCount) { index ->
            val delay = remember(index) { (0..22000).random() }      // старт равномерно
            val duration = remember(index) { 14000 + (0..12000).random() } // 14–26 сек
            val sway = remember(index) { 12f + (0..18).random().toFloat() } // очень лёгкое покачивание

            Snowflake(
                index = index,
                totalCount = snowflakeCount,
                delayMillis = delay,
                durationMillis = duration,
                swayAmount = sway,
                screenHeightDp = screenHeightDp,
                screenWidthDp = screenWidthDp,
                density = density
            )
        }
    }
}

@Composable
private fun Snowflake(
    index: Int,
    totalCount: Int,
    delayMillis: Int,
    durationMillis: Int,
    swayAmount: Float,
    screenHeightDp: Float,
    screenWidthDp: Float,
    density: Float
) {
    val infiniteTransition = rememberInfiniteTransition(label = "snow_$index")

    val y by infiniteTransition.animateFloat(
        initialValue = -0.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis, delayMillis = delayMillis, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "fall"
    )

    val swayPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "sway"
    )

    val baseX = remember(index, totalCount) { index.toFloat() / totalCount }
    val randomOffset = remember(index) { (kotlin.random.Random.nextFloat() - 0.5f) * 0.22f }
    val startX = baseX + randomOffset

    val xOffset = sin(swayPhase * Math.PI * 2.5).toFloat() * swayAmount

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationY = y * screenHeightDp * density
                translationX = (startX + xOffset / screenWidthDp) * screenWidthDp * density
            }
    ) {
        Text(
            text = "❄",
            fontSize = (8 + (index % 9)).sp, // от 8 до 16 sp — аккуратные, не огромные
            color = if (index % 5 == 0) Color(0xFFE3F2FD) else Color.White,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}