package danilfedorov.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DragHandle(
    backgroundColor: Color = colorScheme.onBackground
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = 8.dp, bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .height(4.dp)
                .width(40.dp)
                .background(colorScheme.outline, shapes.medium)
        )
    }
}