package danilfedorov.feature_settings.internal.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
internal fun IconBox(
    onClick: () -> Unit,
    isSelected: Boolean,
    image: @Composable () -> Unit
) {

    var isPressed by remember { mutableStateOf(false) }

    val shadowElevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 6.dp, label = "no label"
    )

    Box(
        modifier = Modifier
            .size(140.dp)
            .shadow(shadowElevation, RoundedCornerShape(20.dp))
            .background(colorScheme.onBackground)
            .clip(RoundedCornerShape(20.dp))
            .border(
                4.dp,
                if (isSelected) colorScheme.primary else Color.Transparent,
                RoundedCornerShape(20.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        image()
    }
}