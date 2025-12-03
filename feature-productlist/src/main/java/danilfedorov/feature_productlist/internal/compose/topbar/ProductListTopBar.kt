@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_productlist.internal.compose.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AppTextField
import danilfedorov.common.core_res.green
import danilfedorov.common.extension.modifier.elevatedCard
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.productlist.R


@Composable
fun ProductListTopBar(
    isMultiSelect: Boolean,
    shoppingListName: String,
    onClickOptions: () -> Unit,
    disableMultiSelect: () -> Unit,
    navHome: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    draftAddProduct: String,
    onChangeAddProduct: (String) -> Unit,
    addProduct: () -> Unit,
    connectionStatus: Boolean?,
) {
    CenterAlignedTopAppBar(
        expandedHeight = 128.dp,
        title = {

            Column(
                Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavigationBackIcon(
                            isMultiSelect = isMultiSelect,
                            disableMultiSelect = disableMultiSelect,
                            navHome = navHome
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = shoppingListName,
                            color = colorScheme.secondary,
                            style = typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (connectionStatus != null) {
                        val bgColor = if (connectionStatus) green
                        else colorScheme.outline
                        Box(
                            Modifier
                                .size(14.dp)
                                .elevatedCard(bgColor)
                                .background(
                                    color = bgColor,
                                    shape = CircleShape
                                )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .noRippleClickable(onClickOptions),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(danilfedorov.common.R.string.menu),
                            tint = colorScheme.secondary,
                            modifier = Modifier
                                .size(26.dp)

                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                AppTextField(
                    value = draftAddProduct,
                    onValueChange = { onChangeAddProduct(it) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_slp),
                            contentDescription = stringResource(R.string.add_product),
                            modifier = Modifier
                                .noRippleClickable {
                                    addProduct()
                                }
                                .padding(end = 8.dp),
                            tint = colorScheme.primary,
                        )
                    },
                    onDone = { addProduct() },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.add_product),
                            style = typography.titleMedium,
                            color = colorScheme.outline
                        )
                    },
                    contentPadding = PaddingValues(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        navigationIcon = {},
        actions = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.onBackground,
            scrolledContainerColor = colorScheme.onBackground,

            ),
        scrollBehavior = scrollBehavior
    )

}