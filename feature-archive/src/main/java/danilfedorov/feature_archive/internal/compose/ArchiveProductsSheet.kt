package danilfedorov.feature_archive.internal.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.common.core_res.background
import danilfedorov.domain_core.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveProductsSheet(
    archiveName: String,
    productList: List<Product>,
    onDelete: () -> Unit,
    onRestore: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )


    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        backgroundColor = if(productList.any { !it.isSelected}) colorScheme.onBackground
        else colorScheme.background
    ) {
        ArchiveProductsSheetContent(
            archiveName = archiveName,
            productList = productList,
            onDelete = onDelete,
            onRestore = onRestore,
            onDismiss = onDismiss
        )
    }
}