@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_shoppinglist.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.noRippleClickable
import io.avdev.feature_shoppinglist.R

@Composable
fun ShoppingListTopBar(
    navArchive: () -> Unit,
    navSettings: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(64.dp)
            .background(color = colorScheme.background)
            .padding(horizontal = 16.dp),
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
                    .height(56.dp)
                    .noRippleClickable {
                        navArchive()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_basket),
                    contentDescription = stringResource(R.string.archive),
                    tint = colorScheme.secondary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Box(
                Modifier
                    .height(56.dp)
                    .noRippleClickable {
                        navSettings()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings),
                    tint = colorScheme.secondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}