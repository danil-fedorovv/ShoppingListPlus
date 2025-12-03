package danilfedorov.feature_productlist.internal.compose.hide_selected_row

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.productlist.R

@Composable
fun FloatingHideSelected(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxWidth(),
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 300),
            initialOffsetY = { -it }
        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 300),
            targetOffsetY = { -it }
        ) + fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Row(
            modifier = modifier
                .wrapContentWidth()
                .noRippleClickable { onClick() }
                .padding(top = 16.dp)
                .background(colorScheme.secondary, shape = shapes.medium)
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.hide_selected_items).uppercase(),
                style = typography.titleLarge.copy(fontSize = 14.sp),
                color = colorScheme.background
            )

            Spacer(Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(R.string.hide_selected_items),
                tint = colorScheme.background
            )

        }
    }
}