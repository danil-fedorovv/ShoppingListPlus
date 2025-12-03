package danilfedorov.feature_shoppinglist.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import danilfedorov.feature_shoppinglist.R

@Composable
fun CircleDone() {
    Box(
        Modifier
            .size(24.dp)
            .background(color = colorScheme.primary, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource(R.drawable.ic_done),
            contentDescription = null,
            tint = colorScheme.background
        )
    }
}