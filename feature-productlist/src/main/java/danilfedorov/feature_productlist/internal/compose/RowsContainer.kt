package danilfedorov.feature_productlist.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
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
import danilfedorov.productlist.R

@Preview(showBackground = true)
@Composable
private fun OptionRowContainerPreview() {
    ShoppingListThemePreview {
        Box(
            Modifier
                .background(colorScheme.background)
                .padding(36.dp),
            contentAlignment = Alignment.Center,
        ) {
            RowsContainer(
                listOf(
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_done_all),
                            text = stringResource(R.string.action_select_all)
                        )
                    },
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_refresh),
                            text = stringResource(R.string.action_clear_selection)
                        )
                    },
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_trash),
                            text = stringResource(R.string.action_delete_selected)
                        )
                    }
                )
            )
        }
    }
 }