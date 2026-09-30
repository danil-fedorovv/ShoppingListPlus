package danilfedorov.common.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun OptionRow(
    modifier: Modifier = Modifier,
    iconTint: Color = colorScheme.outline,
    textColor: Color = colorScheme.secondary,
    painter: Painter,
    text: String,
    subtext: String = "",
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: () -> Unit = {},
    iconSize: Dp = 24.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .noRippleClickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = painter,
            contentDescription = text,
            modifier = Modifier.size(iconSize),
            tint = iconTint
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (subtext.isEmpty()) {
            Text(
                text = text,
                style = typography.titleMedium,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
        } else {


            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = text,
                    style = typography.titleMedium,
                    color = textColor
                )

                Text(
                    text = subtext,
                    style = typography.titleSmall.copy(fontSize = 12.sp),
                    color = colorScheme.outline,

                )
                Spacer(Modifier.height(8.dp))


            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (checked != null && onCheckedChange != null) {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = colorScheme.primary,
                    uncheckedThumbColor = colorScheme.outline,
                    uncheckedTrackColor = Color.Transparent,
                    uncheckedBorderColor = colorScheme.outline
                ),
                modifier = Modifier.scale(0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}