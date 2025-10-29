@file:OptIn(ExperimentalMaterialApi::class)

package danilfedorov.feature_productlist.internal.compose.item


import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import danilfedorov.domain_core.model.Product
import kotlinx.coroutines.delay

@Composable
fun ProductSwipeContainer(
    modifier: Modifier,
    item: Product,
    onDelete: (Product) -> Unit,
    onSelect: (Product) -> Unit,
    content: @Composable (Product) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    var isSelect by remember {
        mutableStateOf(false)
    }
    val swipeDismissState = rememberDismissState(
        confirmStateChange = { value ->
            when (value) {
                DismissValue.DismissedToStart -> {
                    isSelect = true
                    true
                }

                DismissValue.DismissedToEnd -> {
                    isRemoved = true
                    true
                }

                else -> {
                    false
                }
            }
        }
    )

    LaunchedEffect(key1 = isRemoved, key2 = isSelect) {
        if (isRemoved) {
            delay(175)
            onDelete(item)
        }
        if (isSelect) {
            delay(175)
            onSelect(item)

        }
    }

    SwipeToDismiss(
        modifier = modifier,
        state = swipeDismissState,
        background = {
            SwipeContainer(swipeDismissState = swipeDismissState, modifier = modifier)
        },
        dismissContent = { content(item) },
        dismissThresholds = { FractionalThreshold(0.5f) }
    )

}