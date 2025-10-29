package danilfedorov.common.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danilfedorov.common.R
import danilfedorov.common.core_res.ShoppingListThemePreview
import danilfedorov.common.extension.modifier.noRippleClickable


@Composable
@Preview(showBackground = true)
fun ActionRowPreview() {
    ShoppingListThemePreview {
        ActionRow(
            modifier = Modifier,
            true,
            0,
            {},
            {},
            {},
            {})
    }
}

@Composable
fun ActionRow(
    modifier: Modifier = Modifier,
    visible: Boolean,
    multiSelectCount: Int,
    onClickEdit: () -> Unit,
    onClickCopy: () -> Unit,
    onClickDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(
                animationSpec = tween(300),
            ),
            exit = scaleOut(
                animationSpec = tween(300)
            )

        ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(shapes.medium)
                    .background(colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .noRippleClickable(onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.close_product_actions),
                        tint = colorScheme.secondary,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }

                AnimatedContent(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    targetState = multiSelectCount <= 1,
                    transitionSpec = {
                        slideInHorizontally(initialOffsetX = {
                            if (multiSelectCount <= 1) -it else it
                        }) togetherWith slideOutHorizontally(targetOffsetX = {
                            if (multiSelectCount <= 0) it else -it
                        })
                    }
                ) { state ->
                    if (state) {
                        Box(
                            modifier = Modifier
                                .noRippleClickable(onClickEdit),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = stringResource(R.string.edit),
                                tint = colorScheme.secondary,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    } else {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = multiSelectCount.toString(),
                                style = typography.titleLarge,
                                color = colorScheme.secondary
                            )
                        }
                    }


                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .noRippleClickable(onClickCopy),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_copy),
                        contentDescription = stringResource(R.string.copy),
                        tint = colorScheme.secondary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .noRippleClickable(onClickDelete),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = colorScheme.secondary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        }
    }
}