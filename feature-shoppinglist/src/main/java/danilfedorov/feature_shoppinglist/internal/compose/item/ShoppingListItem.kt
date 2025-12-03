@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_shoppinglist.internal.compose.item

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.NoRippleInteractionSource
import danilfedorov.common.core_res.ShoppingListThemePreview
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.feature_shoppinglist.R
import danilfedorov.feature_shoppinglist.internal.compose.CircleDone
import danilfedorov.feature_shoppinglist.internal.compose.EmptyCircle

@Preview(showBackground = true)
@Composable
private fun ShoppingListItemPreview() {
    ShoppingListThemePreview {
        ShoppingListItem(
            shoppingList = ShoppingList(
                0,
                "asdsadsadsadssdasdadsa"
            ),
            selected = 16,
            total = 30,
            navProduct = {},
            onClickOptions = {},
            addMultiSelect = {},
            isMultiSelect = false,
            isMultiSelectMode = false,
            removeMultiSelect = {},
            isLiveEditMode = true
        )
    }
}

@Composable
internal fun ShoppingListItem(
    shoppingList: ShoppingList,
    selected: Int,
    total: Int,
    navProduct: (Long) -> Unit,
    onClickOptions: () -> Unit,
    addMultiSelect: (ShoppingList) -> Unit,
    removeMultiSelect: (ShoppingList) -> Unit,
    isMultiSelectMode: Boolean,
    isMultiSelect: Boolean,
    isLiveEditMode: Boolean,
) {
    val progressIndicator = if (total > 0) {
        selected.toFloat() / total.toFloat()
    } else {
        0f
    }

    Column(
        Modifier
            .fillMaxHeight()
            .combinedClickable(
                onClick = {
                    if (isMultiSelect) {
                        removeMultiSelect(shoppingList)
                    } else {
                        if (isMultiSelectMode) {
                            addMultiSelect(shoppingList)
                        } else {
                            navProduct(shoppingList.id)
                        }
                    }
                },
                onLongClick = {
                    if (!isMultiSelect) {
                        addMultiSelect(shoppingList)
                    }
                },
                indication = null,
                interactionSource = NoRippleInteractionSource
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = shoppingList.name + if (isLiveEditMode)
                    " " + stringResource(R.string.shared_editing) else "",
                style = typography.titleLarge,
                color = colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Transparent)
                    .noRippleClickable {
                        if (isMultiSelect && isMultiSelectMode) {
                            removeMultiSelect(shoppingList)
                        } else if (isMultiSelectMode) {
                            addMultiSelect(shoppingList)
                        } else {
                            onClickOptions()
                        }
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                if (isMultiSelect && isMultiSelectMode) {
                    CircleDone()
                } else if (isMultiSelectMode) {
                    EmptyCircle()
                } else {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "context item menu",
                        tint = colorScheme.secondary
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator(
                trackColor = colorScheme.surface,
                progress = { progressIndicator },
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(shape = CircleShape),
                color = colorScheme.primary,
                drawStopIndicator = { },
                gapSize = 0.dp,
                strokeCap = StrokeCap.Square
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = selected.toString(),
                color = colorScheme.secondary,
                style = typography.titleSmall
            )
            Spacer(Modifier.width(1.dp))
            Text(
                text = "/",
                color = colorScheme.secondary,
                style = typography.titleSmall
            )
            Spacer(Modifier.width(1.dp))
            Text(
                text = total.toString(),
                color = colorScheme.secondary,
                style = typography.titleSmall
            )
        }
    }
}