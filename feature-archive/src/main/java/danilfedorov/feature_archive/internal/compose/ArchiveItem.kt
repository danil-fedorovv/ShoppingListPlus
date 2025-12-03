package danilfedorov.feature_archive.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import danilfedorov.archive.R
import danilfedorov.common.core_res.green
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_archive.internal.util.getDeletedTimeText

@Composable
fun ArchiveItem(
    modifier: Modifier = Modifier,
    listName: String,
    progress: String,
    deleteAt: Long,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onRestore: () -> Unit
) {
    val context = LocalContext.current
    val deleteAtText = getDeletedTimeText(context, deleteAt)

    Column(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .background(color = colorScheme.onBackground, shape = shapes.medium)
            .noRippleClickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = listName,
                style = typography.titleLarge,
                color = colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = progress,
                style = typography.titleMedium,
                color = colorScheme.secondary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = context.getString(R.string.moved_to_trash).uppercase() + " ",
                style = typography.titleSmall.copy(fontSize = 12.sp),
                color = colorScheme.outline
            )
            Text(
                text = deleteAtText.uppercase(),
                style = typography.titleSmall.copy(fontSize = 12.sp),
                color = colorScheme.secondary
            )
        }
        Row(
            modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(R.string.delete).uppercase(),
                style = typography.titleMedium,
                color = colorScheme.error,
                modifier = Modifier
                    .noRippleClickable {
                        onDelete()
                    },
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.restore).uppercase(),
                style = typography.titleMedium,
                color = green,
                modifier = modifier
                    .noRippleClickable {
                        onRestore()
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}