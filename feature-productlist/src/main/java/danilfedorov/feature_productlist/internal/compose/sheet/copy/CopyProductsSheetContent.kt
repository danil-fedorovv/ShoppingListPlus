package danilfedorov.feature_productlist.internal.compose.sheet.copy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.ShoppingListThemePreview
import danilfedorov.common.extension.modifier.elevatedCard
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.domain_core.model.ShoppingList
import io.avdev.productlist.R

@Composable
@Preview
fun CopyProductsSheetContentPreview() {
    ShoppingListThemePreview {
        CopyProductsSheetContent(
            mapOf(),
            {},
            {}
        )
    }
}

@Composable
fun CopyProductsSheetContent(
    shoppingListProgress: Map<ShoppingList, String>,
    createNewListAndCopy: () -> Unit,
    copyItems: (Long) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.copy_products_from_list),
            style = typography.titleLarge,
            color = colorScheme.secondary
        )
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .elevatedCard()
                .noRippleClickable {
                    createNewListAndCopy()
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.new_list),
                color = colorScheme.primary,
                style = typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.copy_in_new_shopping_list),
                modifier = Modifier.size(32.dp),
                tint = colorScheme.primary
            )
        }
    }
    Spacer(Modifier.height(32.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .elevatedCard()
    ) {
        itemsIndexed(shoppingListProgress.entries.toList()) { index, (list, progress) ->
            ShoppingListRow(
                text = list.name,
                subtext = progress,
                onClickCopy = {
                    copyItems(list.id)
                }
            )
            if (index < shoppingListProgress.size - 1) {
                HorizontalDivider(
                    color = colorScheme.onSurface,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }


        }
    }
}
