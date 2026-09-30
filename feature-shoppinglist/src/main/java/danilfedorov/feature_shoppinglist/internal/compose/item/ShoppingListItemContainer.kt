package danilfedorov.feature_shoppinglist.internal.compose.item

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import danilfedorov.domain_core.model.ShoppingList


@Composable
internal fun ShoppingListItemContainer(
    modifier: Modifier = Modifier,
    shoppingList: ShoppingList,
    selected: Int,
    total: Int,
    navProduct: (Long) -> Unit,
    deleteList: () -> Unit,
    shareLiveEditLink: () -> Unit,
    renameList: () -> Unit,
    extendedList: ShoppingList?,
    extendShoppingList: (ShoppingList?) -> Unit,
    onClickOptions: () -> Unit,
    addMultiSelect: (ShoppingList) -> Unit,
    removeMultiSelect: (ShoppingList) -> Unit,
    isMultiSelectMode: Boolean,
    isMultiSelect: Boolean,
    isLiveEditMode: Boolean
) {
    var contentHeight by remember { mutableIntStateOf(0) }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val isExtended by remember (extendedList?.id, shoppingList.id) {
        derivedStateOf { extendedList?.id == shoppingList.id }
    }
    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isExtended) -150f else 0f,
        label = ""
    )

    val draggableState = rememberDraggableState { delta ->
        val newOffset = offsetX + delta
        if (newOffset in -150f..150f) {
            offsetX = newOffset
        }
    }

    fun onDragEnd() {
        if (offsetX < -60f && !isExtended) {
            extendShoppingList(shoppingList)
        } else if (isExtended && offsetX >= -60f) {
            extendShoppingList(null)
            offsetX = 0f
        }
    }
    Box(modifier.wrapContentSize()) {
        SwipeActionsRow(
            contentHeight = contentHeight,
            onEdit = {
                renameList()
                extendShoppingList(null)
            },
            onShare = {
                shareLiveEditLink()
                extendShoppingList(null)
            },
            onDelete = {
                deleteList()
                extendShoppingList(null)
            }
        )
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp)
                .fillMaxHeight()
                .offset(x = animatedOffsetX.dp)
                .draggable(
                    draggableState,
                    Orientation.Horizontal,
                    onDragStopped = {
                        onDragEnd()
                    }
                )
                .background(
                    color = colorScheme.onBackground, shape = shapes.medium
                )
                .border(
                    width = 2.dp,
                    color = if (isMultiSelect) colorScheme.primary else Color.Transparent,
                    shape = shapes.medium
                )
                .onGloballyPositioned { coordinates ->
                    contentHeight = coordinates.size.height
                },
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShoppingListItem(
                shoppingList = shoppingList,
                selected = selected,
                total = total,
                navProduct = { navProduct(shoppingList.id) },
                onClickOptions = onClickOptions,
                addMultiSelect = { addMultiSelect(it) },
                removeMultiSelect = { removeMultiSelect(it) },
                isMultiSelectMode = isMultiSelectMode,
                isMultiSelect = isMultiSelect,
                isLiveEditMode = isLiveEditMode
            )
        }
    }
}