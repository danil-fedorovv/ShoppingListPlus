@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.shared_shoppinglist.presentation.compose.sheet.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.common.compose.LoadAnimation

@Composable
fun LoadingSheet(
    title: String,
) {
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.Hidden }
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {},
        backgroundColor = colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadAnimation()
            Text(
                text = title,
                style = typography.titleMedium,
                color = colorScheme.secondary
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}
