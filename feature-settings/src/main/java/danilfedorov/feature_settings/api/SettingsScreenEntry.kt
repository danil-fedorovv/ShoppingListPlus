package danilfedorov.feature_settings.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_settings.internal.presenter.screen.SettingsScreen
import danilfedorov.feature_settings.internal.presenter.viewmodel.SettingsViewModel
import danilfedorov.feature_settings.internal.presenter.viewmodel.SettingsViewModelFactory

@Composable
fun SettingsScreenEntry(navBack: () -> Unit) {
    val deps = (LocalContext.current.applicationContext as SettingsDepsProvider).settingsDeps
    val viewModel: SettingsViewModel = daggerViewModel {
        SettingsViewModelFactory(deps)
    }

    val darkTheme by viewModel.darkTheme.collectAsState(initial = null)
    val colorIndex by viewModel.colorIndex.collectAsState(initial = null)

    SettingsScreen(
        darkTheme = darkTheme,
        colorIndex = colorIndex,
        changeTheme = { viewModel.setDarkTheme(it) },
        changeColor = { viewModel.setColorIndex(it) },
        navBack = navBack
    )
}