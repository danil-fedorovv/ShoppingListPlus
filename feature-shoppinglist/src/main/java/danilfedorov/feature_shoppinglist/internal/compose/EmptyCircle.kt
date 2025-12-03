package danilfedorov.feature_shoppinglist.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EmptyCircle() {
    Box(
        Modifier
            .size(24.dp)
            .background(color = Color.Transparent, shape = CircleShape)
            .border(width = 2.dp, color = colorScheme.outline, shape = CircleShape),
    )
}