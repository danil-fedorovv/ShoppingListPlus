package danilfedorov.feature_add_product.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun SuggestionItem(
    isAdded: Boolean,
    displayName: String,
    toggleSuggestion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(colorScheme.background)
            .padding(horizontal = 16.dp)
            .noRippleClickable {
                toggleSuggestion()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = displayName,
            style = typography.titleMedium,
            color = colorScheme.secondary,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )

        CircleToggleIconButton(
            isAdded = isAdded,
            toggleSuggestion = toggleSuggestion
        )
    }
}