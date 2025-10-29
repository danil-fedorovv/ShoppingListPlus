package danilfedorov.feature_productlist.internal.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.noRippleClickable
import io.avdev.productlist.R

@Composable
fun SearchInfoColumn(
    modifier: Modifier = Modifier,
    filteredCount: Int,
    itemsCount: Int,
    onClickClear: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if(filteredCount == 0) stringResource(R.string.no_result)
            else stringResource(R.string.found_of_total, filteredCount, itemsCount),
            style = typography.titleMedium,
            color = colorScheme.secondary
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .background(color = colorScheme.primary, shape = shapes.large)
                .noRippleClickable(onClickClear),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.clear_search),
                style = typography.titleMedium,
                color = colorScheme.background,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)
            )
        }
    }
}