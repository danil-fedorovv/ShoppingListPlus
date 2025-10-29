package danilfedorov.feature_productlist.internal.compose.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.avdev.productlist.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SwipeContainer(
    swipeDismissState: DismissState,
    modifier: Modifier = Modifier
) {
    val direction = swipeDismissState.dismissDirection
    val offsetPx = swipeDismissState.offset.value
    val density = LocalDensity.current
    val offsetDp = with(density) { offsetPx.toDp() }

    val (color, imageVector, alignment) = when(direction) {
        DismissDirection.StartToEnd -> Triple(
            colorScheme.error,
            Icons.Rounded.Delete,
            Alignment.CenterStart
        )
        DismissDirection.EndToStart -> Triple(
            colorScheme.primary,
            null,
            Alignment.CenterEnd
        )

        null -> Triple(
            Color.Transparent,
            Icons.Rounded.Delete,
            Alignment.CenterEnd
        )
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color)
        ,
        contentAlignment = alignment
    ) {
        imageVector?.let {
            Icon(
                imageVector = it,
                contentDescription = stringResource(R.string.delete),
                tint = colorScheme.background,
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = if (direction == DismissDirection.StartToEnd) offsetDp - 64.dp else offsetDp + 64.dp)
            )
        }
    }
}