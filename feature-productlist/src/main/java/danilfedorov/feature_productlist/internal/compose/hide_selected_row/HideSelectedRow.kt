package danilfedorov.feature_productlist.internal.compose.hide_selected_row

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danilfedorov.productlist.R

@Composable
@Preview(showBackground = true)
private fun ShoppingListSelectionHeaderPreview() {
    HideSelectedRow(
        expanded = true,
        selectedCount = 10
    )
}

@Composable
fun HideSelectedRow(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    selectedCount: Int
) {
    val label =
        if (expanded) stringResource(R.string.hide_selected_items)
        else stringResource(R.string.show_selected_items)

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "rotation"
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "($selectedCount) $label",
            color = colorScheme.outline,
            style = typography.titleMedium
        )
        Spacer(Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = label,
            tint = colorScheme.outline,
            modifier = Modifier.rotate(rotation)
        )

    }
}
