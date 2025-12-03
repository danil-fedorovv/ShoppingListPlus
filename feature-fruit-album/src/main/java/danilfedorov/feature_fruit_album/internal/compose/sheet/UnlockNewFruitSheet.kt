@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_fruit_album.internal.compose.sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_fruit_album.R

@Composable
fun UnlockNewFruitSheet(
    onDismiss: () -> Unit,
    fruitResId: Int,
    fruitNameResId: Int,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.new_fruit_unlocked_title),
                style = typography.titleLarge,
                color = colorScheme.secondary
            )
            Spacer(Modifier.height(16.dp))
            Image(
                painter = painterResource(fruitResId),
                contentDescription = stringResource(fruitNameResId),
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(fruitNameResId),
                style = typography.titleMedium,
                color = colorScheme.secondary
            )
            Spacer(Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorScheme.primary, shape = shapes.medium)
                    .padding(vertical = 16.dp)
                    .noRippleClickable {
                        onDismiss()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.new_fruit_unlocked_button),
                    style = typography.titleMedium,
                    color = colorScheme.secondary
                )
            }

        }
    }
}