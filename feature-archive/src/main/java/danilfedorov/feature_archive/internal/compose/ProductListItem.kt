package danilfedorov.feature_archive.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AnimatedCheckbox
import danilfedorov.domain_core.model.Product

@Composable
internal fun ProductListItem(
    product: Product,
    backgroundColor: Color,
    textColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            Modifier
                .width(16.dp)
                .heightIn(min = 56.dp)
        )
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(end = 16.dp)
                .weight(6f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product.name,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                softWrap = true
            )
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            if (product.amount.isNotEmpty()) {
                Text(
                    text = "${product.amount} ${product.unit}".trim(),
                    color = colorScheme.outline,
                    style = MaterialTheme.typography.titleMedium,
                    softWrap = true
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(56.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedCheckbox(
                isChecked = product.isSelected,
            )
        }

    }


}