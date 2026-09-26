package danilfedorov.shared_shoppinglist.presentation.compose.sheet.share

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.OptionRow
import danilfedorov.common.compose.RowsContainer
import danilfedorov.common.core_res.ShoppingListThemePreview
import danilfedorov.shared_shoppinglist.R

@Preview(showBackground = true)
@Composable
private fun ShareListSheetContentPreview() {
    ShoppingListThemePreview {
        ShareListSheetContent(
            {}, {}, {},
        )
    }
}

@Composable
fun ShareListSheetContent(
    shareText: () -> Unit,
    liveEditLink: () -> Unit,
    dismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.share_sheet_title),
            style = typography.titleLarge,
            color = colorScheme.secondary
        )
        Spacer(Modifier.height(16.dp))
        RowsContainer(
            optionRows = listOf(
                {
                    OptionRow(
                        painter = painterResource(danilfedorov.common.R.drawable.ic_text),
                        text = stringResource(R.string.share_text_title),
                        onClick = {
                            dismissRequest()
                            shareText()
                        }
                    )
                },
//                {
//                    OptionRow(
//                        painter = painterResource(danilfedorov.common.R.drawable.ic_link),
//                        text = stringResource(R.string.share_link_title),
//                        onClick = {
//                            dismissRequest()
//                            shareLink()
//                        }
//                    )
//                },
                {
                    OptionRow(
                        painter = painterResource(danilfedorov.common.R.drawable.ic_liveedit),
                        text = stringResource(R.string.share_liveedit_title),
                        onClick = {
                            dismissRequest()
                            liveEditLink()
                        }
                    )
                }
            )
        )
    }
}