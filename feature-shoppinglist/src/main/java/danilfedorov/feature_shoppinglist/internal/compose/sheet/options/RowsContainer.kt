package danilfedorov.feature_shoppinglist.internal.compose.sheet.options

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.elevatedCard


@Composable
fun RowsContainer(
    optionRows: List<@Composable () -> Unit>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .elevatedCard()
        ) {
        optionRows.forEachIndexed { index, optionRow ->
            optionRow()
            if (index < optionRows.lastIndex) {
                HorizontalDivider(
                    color = colorScheme.onSurface,
                    thickness = 1 .dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}