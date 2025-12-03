package danilfedorov.feature_shoppinglist.internal.compose.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.blue
import danilfedorov.common.core_res.green
import danilfedorov.common.core_res.red
import danilfedorov.feature_shoppinglist.internal.compose.ActionButton

@Composable
internal fun BoxScope.SwipeActionsRow(
    contentHeight: Int,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    val density = LocalDensity.current

    Row(
        modifier = Modifier
            .height(with(density) { contentHeight.toDp() })
            .padding(top = 8.dp, end = 16.dp)
            .align(Alignment.CenterEnd)
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .width(50.dp)
                .background(color = blue)
        )
        ActionButton(
            color = blue,
            width = 50.dp,
            painterId = danilfedorov.common.R.drawable.ic_edit,
            contentDescription = "edit",
            onClick = onEdit
        )

        ActionButton(
            color = green,
            width = 50.dp,
            painterId = danilfedorov.common.R.drawable.ic_share,
            contentDescription = "share",
            onClick = onShare
        )

        ActionButton(
            color = red,
            width = 50.dp,
            cornerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            painterId = danilfedorov.common.R.drawable.ic_trash,
            contentDescription = "delete",
            onClick = onDelete
        )
    }
}