package danilfedorov.feature_archive.internal.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danilfedorov.archive.R
import danilfedorov.common.compose.ActionButtonRow
import danilfedorov.common.core_res.ShoppingListThemePreview
import danilfedorov.common.core_res.onBackground
import danilfedorov.domain_core.model.Product

@Preview
@Composable
private fun ArchiveProductsSheetContentPreview() {
    ShoppingListThemePreview {
        ArchiveProductsSheetContent(
            "",
            listOf(
                Product(listId = 1, name = "PIPIDASTID"),
                Product(listId = 1, name = "PIPIDASTID"),
                Product(listId = 1, name = "PIPIDASTID"),
                Product(listId = 1, name = "PIPIDASTID"),
                Product(listId = 1, name = "PIPIDASTID"),
                Product(listId = 1, name = "PIPIDASTID", isSelected = true),
                Product(listId = 1, name = "PIPIDASTID"),
                Product(listId = 1, name = "PIPIDASTID"),
                Product(listId = 1, name = "PIPIDASTID"),
            ), {}, {}, {}
        )
    }
}

@Composable
fun ArchiveProductsSheetContent(
    archiveName: String,
    productList: List<Product>,
    onDelete: () -> Unit,
    onRestore: () -> Unit,
    onDismiss: () -> Unit,
) {

    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = archiveName,
            style = typography.titleLarge,
            color = colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()

        ) {
            if (productList.isEmpty()) {
                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painterResource(danilfedorov.common.R.drawable.img_empty_basket),
                            contentDescription = null,
                            modifier = Modifier.size(128.dp)
                        )
                        Text(
                            text = stringResource(R.string.this_list_is_empty),
                            style = typography.titleMedium,
                            color = colorScheme.outline,
                            modifier = Modifier
                        )
                    }
                }
            }

            items(productList.sortedBy { it.isSelected }) { product ->
                if (!product.isSelected) {
                    Column {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        )
                        ProductListItem(
                            product = product,
                            backgroundColor = colorScheme.onBackground,
                            textColor = colorScheme.secondary
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
            item {
                ActionButtonRow(
                    leftText = stringResource(R.string.delete).uppercase(),
                    onLeftClick = {
                        onDismiss()
                        onDelete()
                    },
                    rightText = stringResource(R.string.restore).uppercase(),
                    onRightClick = {
                        onDismiss()
                        onRestore()
                    },
                    background = if (productList.any { it.isSelected })
                        colorScheme.background else onBackground
                )

            }
        }
    }
}
