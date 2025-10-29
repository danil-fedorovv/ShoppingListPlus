package danilfedorov.feature_productlist.internal.compose.sheet.options

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.avdev.productlist.R
import danilfedorov.feature_productlist.internal.compose.RowsContainer

@Composable
fun OptionsSheetContent(
    search: () -> Unit,
    modifier: Modifier = Modifier,
    shareList: () -> Unit,
    renameList: () -> Unit,
    selectAll: () -> Unit,
    clearSelection: () -> Unit,
    deleteAllSelected: () -> Unit,
    isCheckedSort: Boolean,
    switchSort: (Boolean) -> Unit
) {
    val optionRowsContainers: List<@Composable () -> Unit> = listOf(
        {
            RowsContainer(
                listOf(
                    {
                        OptionRow(
                            icon = Icons.Rounded.Search,
                            text = stringResource(R.string.search_in_list),
                            onClick = { search() }
                        )
                    }
                )
            )
        },
        {
            RowsContainer(
                listOf(
                    {
                        OptionRow(
                            icon = Icons.Rounded.Edit,
                            text = stringResource(R.string.rename_list),
                            onClick = { renameList() }
                        )
                    },
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_share),
                            text = stringResource(R.string.share),
                            onClick = { shareList() }
                        )
                    }
                )
            )
        },
        {
            RowsContainer(
                listOf(
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_sort_a),
                            text = stringResource(R.string.sort_by_alphabet),
                            onCheckedChange = { switchSort(it) },
                            checked = isCheckedSort
                        )
                    }
                )
            )
        },
        {
            RowsContainer(
                listOf(
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_refresh),
                            text = stringResource(R.string.action_clear_selection),
                            onClick = { clearSelection() }
                        )
                    },
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_done_all),
                            text = stringResource(R.string.action_select_all),
                            onClick = { selectAll() }
                        )
                    },
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_trash),
                            text = stringResource(R.string.action_delete_selected),
                            iconTint = colorScheme.error,
                            textColor = colorScheme.error,
                            onClick = { deleteAllSelected() }
                        )
                    }
                )
            )
        }
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        optionRowsContainers.forEachIndexed { index, optionRowsContainer ->
            optionRowsContainer()
            if (index <= optionRowsContainers.lastIndex) {
                Spacer(Modifier.height(24.dp))
            }

        }
    }
}