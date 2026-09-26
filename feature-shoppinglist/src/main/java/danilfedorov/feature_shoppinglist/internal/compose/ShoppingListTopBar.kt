@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_shoppinglist.internal.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_shoppinglist.R

@Preview
@Composable
fun ShoppingListTopBarPreview() {
    ShoppingListTopBar({}, {}, 4)
}

@Composable
fun ShoppingListTopBar(
    navArchive: () -> Unit,
    navSettings: () -> Unit,
    archivedCount: Int
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
                modifier = Modifier
                    .height(containerHeight)
                    .width(40.dp)
                    .noRippleClickable {
                        navArchive()
                    }

            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_archive),
                    contentDescription = stringResource(R.string.archive),
                    tint = colorScheme.secondary,
                    modifier = Modifier
                        .size(elementSize)
                        .align(Alignment.Center)
                )

                if (archivedCount != 0) {
                    Badge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 12.dp)
                            .padding(end = 3.dp),
                        containerColor = colorScheme.primary
                    ) {
                        Text(
                            text = if(archivedCount<99) archivedCount.toString() else 99.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                        )
                    }
                }


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
                        .padding(start = 4.dp, end = 16.dp)
                        .size(elementSize)
                )
            }
        }
    }
}