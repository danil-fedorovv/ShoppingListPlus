package danilfedorov.feature_premium.internal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import danilfedorov.common.core_res.TextWhite
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_premium.R
import danilfedorov.feature_premium.internal.enum.PremiumType

@Composable
fun PremiumBottomBar(
    isPremium: Boolean,
    premiumType: PremiumType,
    premiumExpirationDate: String?,
    selectType: (PremiumType) -> Unit,
    subscribe: () -> Unit,
    onClickDisableAutoRenewal: () -> Unit
) {
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (!isPremium) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriceCard(
                    modifier = Modifier.weight(1f),
                    isSelected = premiumType == PremiumType.MONTHLY,
                    select = { selectType(PremiumType.MONTHLY) },
                    amount = 30,
                    units = stringResource(R.string.days),
                    price = PremiumType.MONTHLY.price
                )
                PriceCard(
                    modifier = Modifier.weight(1f),
                    isSelected = premiumType == PremiumType.HALF_YEAR,
                    select = { selectType(PremiumType.HALF_YEAR) },
                    amount = 6,
                    units = stringResource(R.string.months),
                    price = PremiumType.HALF_YEAR.price
                )
                PriceCard(
                    modifier = Modifier.weight(1f),
                    isSelected = premiumType == PremiumType.YEARLY,
                    select = { selectType(PremiumType.YEARLY) },
                    amount = 12,
                    units = stringResource(R.string.months),
                    price = PremiumType.YEARLY.price
                )
            }
        } else {
            premiumExpirationDate?.let {
                Text(
                    text = stringResource(R.string.subscription_active_until) + " " + it,
                    style = typography.titleMedium,
                    color = colorScheme.outline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = colorScheme.primary, shape = shapes.medium)
                .noRippleClickable {
                    if(!isPremium) subscribe()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if(!isPremium) stringResource(R.string.premium_cta)
                else stringResource(R.string.premium_status_active),
                style = typography.titleLarge,
                color = TextWhite,
                modifier = Modifier.padding(vertical = 16.dp)

            )
        }

    }

}