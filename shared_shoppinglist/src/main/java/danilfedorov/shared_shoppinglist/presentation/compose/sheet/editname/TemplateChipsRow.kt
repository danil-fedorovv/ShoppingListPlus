package danilfedorov.shared_shoppinglist.presentation.compose.sheet.editname

import android.R.attr.label
import android.R.attr.shadowColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.background
import danilfedorov.common.extension.modifier.elevatedCard
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.shared_shoppinglist.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun TemplateChipsRow(
    onTemplateSelected: (String) -> Unit,
    value: String
) {
    val today = remember {
        val formatter = SimpleDateFormat("M.d", Locale.getDefault())
        formatter.format(Date())
    }

    val templates = listOf(
        stringResource(R.string.template_groceries),
        today,
        stringResource(R.string.template_shopping),
        stringResource(R.string.template_weekly),
        stringResource(R.string.template_weekend),
        stringResource(R.string.template_pharmacy),
        stringResource(R.string.template_home),
        stringResource(R.string.template_dacha),
        stringResource(R.string.template_holiday)
    )

    Column {
        Text(
            text = stringResource(R.string.new_list_templates_hint),
            style = typography.titleMedium,
            color = colorScheme.secondary
        )
        Spacer(Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(templates) { template ->
                Box(
                    modifier = Modifier
                        .border(
                            width = if (value == template) 2.dp else 1.dp,
                            color = if (value == template) colorScheme.primary else colorScheme.surface,
                            shape = shapes.extraLarge
                        )
                        .background(
                            color = colorScheme.onBackground,
                            shape = shapes.extraLarge
                        )
                        .noRippleClickable { onTemplateSelected(template) }
                ) {
                    Text(
                        text = template,
                        style = typography.titleMedium,
                        color = if (value == template) colorScheme.primary else colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                }
            }
        }
    }
}