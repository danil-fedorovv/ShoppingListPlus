package danilfedorov.feature_productlist.internal.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AppTextField
import danilfedorov.common.extension.modifier.noRippleClickable
import io.avdev.productlist.R
import danilfedorov.feature_productlist.internal.compose.topbar.NavigationBackIcon

@Composable
fun ProductSearchRow(
    modifier: Modifier = Modifier,
    searchValue: String,
    onChangeSearchValue: (String) -> Unit,
    isMultiSelect: Boolean,
    disableMultiSelect: () -> Unit,
    navBack: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorScheme.onBackground)
            .statusBarsPadding()
            .padding(vertical = 16.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationBackIcon(
            isMultiSelect = isMultiSelect,
            disableMultiSelect = disableMultiSelect,
            navHome = navBack
        )
        Spacer(Modifier.width(8.dp))
        AppTextField(
            modifier = modifier.weight(1f),
            value = searchValue,
            onValueChange = onChangeSearchValue,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.add_product),
                    tint = colorScheme.outline
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    style = typography.titleMedium,
                    color = colorScheme.outline
                )
            },
            trailingIcon = if (searchValue.isNotEmpty()) {
                {
                    Box(
                        Modifier
                            .size(20.dp)
                            .background(shape = CircleShape, color = colorScheme.outline)
                            .noRippleClickable {
                                onChangeSearchValue("")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cross),
                            contentDescription = stringResource(R.string.clear_search),
                            tint = colorScheme.surface,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            } else null
        )
    }
}



























