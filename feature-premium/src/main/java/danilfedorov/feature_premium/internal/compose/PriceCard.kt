package danilfedorov.feature_premium.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import danilfedorov.common.core_res.TextWhite
import danilfedorov.common.extension.modifier.elevatedCard
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun PriceCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    select: () -> Unit,
    amount: Int,
    units: String,
    price: String
) {
    val backgroundColor = if (isSelected) colorScheme.primary else colorScheme.onBackground
    val shape = shapes.medium
    val textColor = if (isSelected) TextWhite else colorScheme.secondary

    Column(
        modifier
            .elevatedCard(colorScheme.primary)
            .background(shape = shape, color = backgroundColor)
            .noRippleClickable {
                select()
            },
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = amount.toString(),
            style = typography.titleLarge.copy(fontSize = 30.sp),
            color = textColor,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = units,
            style = typography.titleMedium,
            color = textColor,
        )
        Text(
            text = price,
            style = typography.titleSmall,
            color = textColor,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
    }
}