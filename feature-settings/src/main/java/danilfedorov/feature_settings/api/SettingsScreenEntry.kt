package danilfedorov.feature_settings.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_settings.internal.presenter.screen.SettingsScreen
import danilfedorov.feature_settings.internal.presenter.viewmodel.SettingsViewModel
import danilfedorov.feature_settings.internal.presenter.viewmodel.SettingsViewModelFactory

@Composable
fun SettingsScreenEntry(
    navBack: () -> Unit,
    navFruitCollection: () -> Unit,
    navPremium: () -> Unit,
) {
    val context = LocalContext.current
    val deps = (context.applicationContext as SettingsDepsProvider).settingsDeps
    val viewModel: SettingsViewModel = daggerViewModel {
        SettingsViewModelFactory(navBack, navFruitCollection, navPremium, deps, context)
    }

    val screenState = viewModel.screenState.collectAsState()

    SettingsScreen(
        screenState = screenState.value,
        action = { viewModel.action(it) },
        effect = viewModel.effect
    )
}