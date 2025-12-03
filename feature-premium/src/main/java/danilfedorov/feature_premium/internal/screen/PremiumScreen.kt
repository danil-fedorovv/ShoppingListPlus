package danilfedorov.feature_premium.internal.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import danilfedorov.common.compose.AppTopBar
import danilfedorov.common.compose.OptionRow
import danilfedorov.common.compose.RowsContainer
import danilfedorov.common.core_res.ShoppingListThemePreview
import danilfedorov.common.extension.modifier.elevatedCard
import danilfedorov.feature_premium.R
import danilfedorov.feature_premium.internal.compose.PremiumBottomBar
import danilfedorov.feature_premium.internal.compose.PremiumCongratsBottomSheet
import danilfedorov.feature_premium.internal.mvi.Action
import danilfedorov.feature_premium.internal.mvi.ScreenState


@Composable
@Preview(showBackground = true)
fun PremiumScreenPreview() {
    ShoppingListThemePreview {
        PremiumScreen(
            ScreenState(isPremium = true, premiumExpirationDate = "19 January 2026"), {}
        )
    }


}

@Composable
fun PremiumScreen(
    screenState: ScreenState,
    action: (Action) -> Unit,
) {

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.premium),
                navBack = { action(Action.NavHome) }
            )
        },
        bottomBar = {
            PremiumBottomBar(
                isPremium = screenState.isPremium,
                premiumType = screenState.subscriptionType,
                premiumExpirationDate = screenState.premiumExpirationDate,
                selectType = { action(Action.SelectType(it)) },
                subscribe = { action(Action.Subscribe()) },
                {}
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Spacer(Modifier.height(16.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .elevatedCard(colorScheme.primary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(danilfedorov.common.R.drawable.ic_diamond),
                    contentDescription = stringResource(R.string.premium),
                    tint = colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .size(36.dp)
                )
                Text(
                    text =
                        if (screenState.isPremium) stringResource(R.string.premium_thanks_title)
                        else stringResource(R.string.premium_title),
                    style = typography.titleLarge.copy(fontSize = 28.sp, lineHeight = 36.sp),
                    color = colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (screenState.isPremium) stringResource(R.string.premium_thanks_subtitle)
                    else stringResource(R.string.premium_description),
                    style = typography.titleMedium,
                    color = colorScheme.secondary,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 16.dp)
                        .padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center

                )
            }
            Spacer(Modifier.height(16.dp))
            RowsContainer(
                listOf(
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_no_ads),
                            text = stringResource(R.string.premium_feature_no_ads)
                        )
                    },
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_flower),
                            text = stringResource(R.string.premium_feature_customization)
                        )
                    },
                    {
                        OptionRow(
                            painter = painterResource(R.drawable.ic_heart),
                            text = stringResource(R.string.premium_feature_priority_support)
                        )
                    },

                    ),
                verticalRowPadding = 16.dp,
                dividerThickness = 0.5.dp,
                color = colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
        }

    }
    if(screenState.isCongratsSheetVisible) {
        PremiumCongratsBottomSheet(
            onDismiss = { action(Action.HideCongratsSheet)},
            navHome = { action(Action.NavHome)}
        )
    }
}