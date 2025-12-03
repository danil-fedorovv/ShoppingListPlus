package danilfedorov.common.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.elevatedCard

@Composable
fun RowsContainer(
    optionRows: List<@Composable () -> Unit>,
    verticalRowPadding: Dp = 0.dp,
    dividerThickness: Dp = 1.dp,
    color: Color = colorScheme.surface,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .elevatedCard(color)
    ) {
        optionRows.forEachIndexed { index, optionRow ->
            Spacer(Modifier.height(verticalRowPadding))
            optionRow()
            Spacer(Modifier.height(verticalRowPadding))
            if (index < optionRows.lastIndex) {
                HorizontalDivider(
                    color = color,
                    thickness = dividerThickness,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .elevatedCard(color)
                )
            }
        }
    }

}
