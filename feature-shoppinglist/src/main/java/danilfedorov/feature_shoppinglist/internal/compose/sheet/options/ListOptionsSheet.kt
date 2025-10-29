package danilfedorov.feature_shoppinglist.internal.compose.sheet.options

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
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
import io.avdev.feature_shoppinglist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOptionsSheet(
    listName: String,
    onRename: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    AppModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        backgroundColor = colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = listName,
                style = typography.titleLarge,
                color = colorScheme.secondary,
            )
            Spacer(Modifier.height(16.dp))
            RowsContainer(
                listOf(
                    {
                        OptionRow(
                            icon = Icons.Rounded.Edit,
                            text = stringResource(R.string.rename),
                            onClick = {
                                onDismiss()
                                onRename()
                            }
                        )
                    },
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_share),
                            text = stringResource(R.string.share),
                            onClick = {
                                onDismiss()
                                onShare()
                            }
                        )
                    }, {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_copy),
                            text = stringResource(R.string.copy),
                            onClick = {
                                onDismiss()
                                onCopy()
                            }
                        )
                    }
                )
            )
            Spacer(Modifier.height(8.dp))
            RowsContainer(
                listOf(
                    {
                        OptionRow(
                            painter = painterResource(danilfedorov.common.R.drawable.ic_trash),
                            text = stringResource(R.string.delete),
                            iconTint = colorScheme.error,
                            textColor = colorScheme.error,
                            onClick = {
                                onDismiss()
                                onDelete()
                            }
                        )
                    }
                ))
        }
    }
}

