@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_settings.internal.compose.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.common.core_res.TextWhite
import danilfedorov.common.core_res.colors
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_settings.R

@Composable
fun ChangeColorSheet(
    colorIndex: Int,
    saveColor: (Int) -> Unit,
    isPremium: Boolean,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var tempColor by remember { mutableStateOf(colorIndex) }

            Text(
                text = stringResource(R.string.settings_primary_color),
                style = typography.titleLarge,
                color = colorScheme.secondary
            )
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 48.dp),
                contentPadding = PaddingValues(20.dp, 16.dp, 20.dp, 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                itemsIndexed(colors) { index, color ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .border(4.dp, color, CircleShape)
                            .noRippleClickable {
                                tempColor = index
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (tempColor == index) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(color = colors[tempColor], shape = shapes.medium)
                    .noRippleClickable {
                        if (tempColor != colorIndex) {
                            saveColor(tempColor)
                        }
                        onDismissRequest()
                    },
            ) {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(danilfedorov.common.R.string.save),
                    style = typography.titleLarge,
                    color = TextWhite,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (!isPremium) {
                    Text(
                        text = stringResource(danilfedorov.common.R.string.watch_short_ad),
                        style = typography.titleSmall,
                        color = TextWhite,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(8.dp))
            }


        }
    }
}