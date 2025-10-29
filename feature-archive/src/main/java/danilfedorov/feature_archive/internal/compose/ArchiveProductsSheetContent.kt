package danilfedorov.feature_archive.internal.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.domain_core.model.Product
import io.avdev.archive.R

@Composable
fun ArchiveProductsSheetContent(
    archiveName: String,
    productList: List<Product>,
) {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = archiveName,
            style = typography.titleLarge,
            color = colorScheme.secondary
        )
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()

        ) {
            if (productList.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.this_list_is_empty),
                            style = typography.titleMedium,
                            color = colorScheme.outline,
                            modifier = Modifier
                        )
                    }
                }
            }

            itemsIndexed(productList.sortedBy { it.isSelected }) { index, product ->
                if (!product.isSelected) {
                    Column {
                        ProductListItem(
                            product = product,
                            backgroundColor = colorScheme.onBackground,
                            textColor = colorScheme.secondary
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    ProductListItem(
                        product = product,
                        backgroundColor = colorScheme.background,
                        textColor = colorScheme.outline
                    )
                }
            }
        }
    }

}