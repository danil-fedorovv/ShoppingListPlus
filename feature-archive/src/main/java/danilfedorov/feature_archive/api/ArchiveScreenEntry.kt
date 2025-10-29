package danilfedorov.feature_archive.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_archive.internal.presenter.screen.ArchiveScreen
import danilfedorov.feature_archive.internal.presenter.viewmodel.ArchiveViewModel
import danilfedorov.feature_archive.internal.presenter.viewmodel.ArchiveViewModelFactory

@Composable
fun ArchiveScreenEntry(
    navHome: () -> Unit
) {
    val deps = (LocalContext.current.applicationContext as ArchiveDepsProvider).archiveDeps
    val viewModel = daggerViewModel<ArchiveViewModel> {
        ArchiveViewModelFactory(navHome, deps)
    }

    val state = viewModel.screenState.collectAsState()

    ArchiveScreen(
        screenState = state.value,
        action = { viewModel.action(it) },
        effect = viewModel.effect
    )
}