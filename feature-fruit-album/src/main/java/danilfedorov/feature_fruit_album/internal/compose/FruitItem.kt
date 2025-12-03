package danilfedorov.feature_fruit_album.internal.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.outline
import danilfedorov.common.extension.modifier.elevatedCard
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun FruitItem(
    modifier: Modifier = Modifier,
    fruitResId: Int,
    fruitName: String?,
    isUnlocked: Boolean,
    isEnabled: Boolean,
    enable: () -> Unit,
) {

    val colorFilter = remember(isUnlocked) {
        if (isUnlocked) null else ColorFilter.tint(outline)
    }

    val enabledColor = if (isEnabled) colorScheme.primary else Color.Transparent


    Column(
        modifier = modifier
            .elevatedCard(
                enabledColor
            )
            .background(
                color = colorScheme.surface,
                shape = shapes.medium
            )
            .border(width = 1.dp, shape = shapes.medium, color = enabledColor)
            .noRippleClickable {
                enable()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(fruitResId),
            contentDescription = null,
            modifier = Modifier.size(75.dp),
            colorFilter = colorFilter
        )
        Spacer(Modifier.height(8.dp))
        fruitName?.let {
            Text(
                text = it,
                style = typography.titleSmall,
                color = colorScheme.secondary
            )
        }
    }
}