package danilfedorov.common.compose

import android.R.attr.shadowColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.elevatedCard
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun ActionButtonRow(
    leftText: String,
    rightText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    background: Color = colorScheme.surface,
    verticalPadding: Dp = 16.dp
) {
    Row(
        modifier = Modifier
            .background(background)
            .padding(vertical = verticalPadding)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .dropShadow(
                    shape = shapes.medium,
                    shadow = Shadow(
                        radius = 4.dp,
                        spread = 0.5.dp,
                        color = colorScheme.primary
                    )
                )
                .background(background, shape = shapes.medium)
                .noRippleClickable {
                    onLeftClick()
                }
                ,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = leftText,
                style = typography.titleMedium,
                color = colorScheme.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .background(colorScheme.primary, shape = shapes.medium)
                .noRippleClickable {
                    onRightClick()
                }
                .dropShadow(
                    shape = shapes.medium,
                    shadow = Shadow(
                        radius = 4.dp,
                        spread = 0.5.dp,
                        color = colorScheme.primary
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rightText,
                style = typography.titleMedium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}