package danilfedorov.feature_productlist.internal.compose.sheet.edit_product

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.noRippleClickable
import io.avdev.productlist.R

@Composable
fun UnitSelection(
    unitValue: String,
    onChangeUnit: (String) -> Unit,
) {
    val defaultUnits = listOf(
        stringResource(R.string.pcs),
        stringResource(R.string.kg),
        stringResource(R.string.l),
        stringResource(R.string.g),
        stringResource(R.string.ml),
    )


    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.units).uppercase(),
            color = colorScheme.outline,
            style = typography.titleLarge
        )
        Spacer(Modifier.width(16.dp))
        defaultUnits.forEach { unit ->
            val highlight = colorScheme.primary.copy(alpha = 0.2f)
            val highlightOpaque = highlight.compositeOver(colorScheme.surface)

            val animatedBackground by animateColorAsState(
                targetValue = if (unitValue == unit) highlightOpaque else colorScheme.surface,
                animationSpec = tween(150)
            )
            Box(
                modifier = Modifier
                    .background(
                        color = animatedBackground,
                        shape = shapes.small
                    )
                    .padding(4.dp)
                    .noRippleClickable {
                        onChangeUnit(unit)
                    }
            ) {
                Text(
                    text = unit,
                    color = colorScheme.secondary,
                    modifier = Modifier.padding(5.dp),
                    style = typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.width(4.dp))
        }
    }
}