@file:OptIn(ExperimentalMaterialApi::class)

package danilfedorov.feature_productlist.internal.compose.item

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AnimatedCheckbox
import danilfedorov.common.compose.NoRippleInteractionSource
import danilfedorov.domain_core.model.Product
import danilfedorov.feature_productlist.internal.presenter.mvi.Action
import danilfedorov.feature_productlist.internal.presenter.mvi.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ProductListItem(
    product: Product,
    screenState: ScreenState,
    action: (Action) -> Unit,
    isInMultiSelect: Boolean
) {
    var isSelected by remember(product.roomId) { mutableStateOf(product.isSelected) }

    val backgroundColor by animateColorAsState(
        targetValue = if(isSelected) colorScheme.background else colorScheme.onBackground,
        animationSpec = tween(durationMillis = 350)
    )

    val textColor by animateColorAsState(
        targetValue = if(isSelected) colorScheme.outline else colorScheme.secondary,
        animationSpec = tween(durationMillis = 350)
    )

    val coroutineScope = rememberCoroutineScope()

    val translucentPrimary = colorScheme.primary.copy(alpha = 0.1f)
    val highlightOpaque = translucentPrimary.compositeOver(backgroundColor)

    val animatedBackground by animateColorAsState(
        targetValue = if (isInMultiSelect) highlightOpaque else backgroundColor,
        animationSpec = tween(durationMillis = 350)
    )


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(animatedBackground)
            .combinedClickable(
                onLongClick = {
                    if (!isInMultiSelect) {
                        action(Action.AddToMultiSelect(product))
                    }
                },
                onClick = {
                    if (screenState.multiSelectProducts.isEmpty()) {
                        action(Action.InitEditProduct(product))
                    } else {
                        if (isInMultiSelect) {
                            action(Action.DeleteFromMultiSelect(product))
                        } else {
                            action(Action.AddToMultiSelect(product))
                        }
                    }
                },
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            Modifier
                .width(16.dp)
                .heightIn(min = 56.dp)
        )
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(end = 16.dp)
                .weight(6f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product.name,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                softWrap = true
            )
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            if (product.amount.isNotEmpty()) {
                Text(
                    text = "${product.amount} ${product.unit}".trim(),
                    color = colorScheme.outline,
                    style = MaterialTheme.typography.titleMedium,
                    softWrap = true
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(56.dp)
                .clickable(
                    interactionSource = NoRippleInteractionSource,
                    indication = null
                ) {
                    isSelected = !isSelected
                    coroutineScope.launch {
                        delay(350)
                        action(Action.ToggleSelect(product))
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            AnimatedCheckbox(
                isChecked = isSelected,
            )
        }

    }


}