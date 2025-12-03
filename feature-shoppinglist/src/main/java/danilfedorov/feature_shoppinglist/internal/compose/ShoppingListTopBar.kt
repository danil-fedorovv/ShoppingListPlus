@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_shoppinglist.internal.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_shoppinglist.R

@Preview
@Composable
fun ShoppingListTopBarPreview() {
    ShoppingListTopBar({},{},{}, {})
}

@Composable
fun ShoppingListTopBar(
    navFruitAlbum: () -> Unit,
    navSubscription: () -> Unit,
    navArchive: () -> Unit,
    navSettings: () -> Unit
) {

    val containerHeight = 56.dp
    val elementSize = 26.dp

    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(64.dp)
            .background(color = colorScheme.background)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.my_lists),
            color = colorScheme.secondary,
            style = typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row {
            Box(
                Modifier
                    .height(containerHeight)
                    .noRippleClickable {
                        navFruitAlbum()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(danilfedorov.common.R.drawable.grape),
                    contentDescription = stringResource(R.string.subscription),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(elementSize)
                )
            }
            Box(
                Modifier
                    .height(containerHeight)
                    .noRippleClickable {
                        navSubscription()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(danilfedorov.common.R.drawable.ic_diamond),
                    contentDescription = stringResource(R.string.subscription),
                    tint = colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 2.dp)
                        .size(elementSize)
                )
            }
            Box(
                Modifier
                    .height(containerHeight)
                    .noRippleClickable {
                        navArchive()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_basket),
                    contentDescription = stringResource(R.string.archive),
                    tint = colorScheme.secondary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(elementSize)
                )
            }
            Box(
                Modifier
                    .height(containerHeight)
                    .noRippleClickable {
                        navSettings()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings),
                    tint = colorScheme.secondary,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(elementSize)
                )
            }
        }
    }
}