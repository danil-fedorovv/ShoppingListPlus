@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_premium.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import danilfedorov.common.compose.AppModalBottomSheet
import danilfedorov.common.core_res.TextWhite
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_premium.R

@Composable
fun PremiumCongratsBottomSheet(
    onDismiss: () -> Unit,
    navHome: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.premium_congrats_title),
                style = typography.titleLarge.copy(fontSize = 28.sp, lineHeight = 36.sp),
                color = colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.premium_congrats_message),
                style = typography.titleMedium,
                color = colorScheme.secondary,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(color = colorScheme.primary, shape = shapes.medium)
                    .noRippleClickable {
                        onDismiss()
                        navHome()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.premium_congrats_button),
                    style = typography.titleLarge,
                    color = TextWhite,
                    modifier = Modifier.padding(vertical = 16.dp)

                )
            }
        }

    }
}