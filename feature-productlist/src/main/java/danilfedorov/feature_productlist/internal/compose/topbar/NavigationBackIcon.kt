package danilfedorov.feature_productlist.internal.compose.topbar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.productlist.R

@Composable
fun NavigationBackIcon(
    isMultiSelect: Boolean,
    disableMultiSelect: () -> Unit,
    navHome: () -> Unit

) {
    val rotation by animateFloatAsState(
        targetValue = if (isMultiSelect) 180f else 0f,
        label = "rotation"
    )

    Icon(
        painter = if (isMultiSelect) painterResource(danilfedorov.common.R.drawable.ic_cross)
        else painterResource(danilfedorov.common.R.drawable.ic_arrow_back),
        contentDescription = stringResource(danilfedorov.common.R.string.navback),
        tint = colorScheme.secondary,
        modifier = Modifier
            .size(32.dp)
            .noRippleClickable {
                if(isMultiSelect) disableMultiSelect()
                else navHome()
            }
            .graphicsLayer(
                rotationZ = rotation
            )

    )
}