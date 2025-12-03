package danilfedorov.feature_premium.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_premium.internal.screen.PremiumScreen
import danilfedorov.feature_premium.internal.viewmodel.PremiumViewModel
import danilfedorov.feature_premium.internal.viewmodel.PremiumViewModelFactory

@Composable
fun PremiumScreenEntry(
    navHome: () -> Unit
) {

    val deps = (LocalContext.current.applicationContext as PremiumDepsProvider).premiumDeps
    val viewModel: PremiumViewModel = daggerViewModel {
        PremiumViewModelFactory(deps, navHome)
    }

    val screenState = viewModel.screenState.collectAsState().value

    PremiumScreen(
        screenState = screenState,
        action = viewModel::action
    )
}