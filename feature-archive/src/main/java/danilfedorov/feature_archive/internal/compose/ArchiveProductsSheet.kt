package danilfedorov.feature_archive.internal.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.domain_core.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveProductsSheet(
    archiveName: String,
    productList: List<Product>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        backgroundColor = colorScheme.onBackground
    ) {
        ArchiveProductsSheetContent(
            archiveName = archiveName,
            productList = productList
        )
    }
}