package danilfedorov.feature_fruit_album.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_fruit_album.internal.screen.FruitAlbumScreen
import danilfedorov.feature_fruit_album.internal.viewmodel.FruitAlbumViewModel
import danilfedorov.feature_fruit_album.internal.viewmodel.FruitAlbumViewModelProvider

@Composable
fun FruitAlbumScreenEntry(
    navHome: () -> Unit
) {
    val context = LocalContext.current
    val deps = (context.applicationContext as FruitAlbumDepsProvider).fruitAlbumDeps
    val viewModel = daggerViewModel<FruitAlbumViewModel> {
        FruitAlbumViewModelProvider(deps, navHome, context)
    }

    val screenState = viewModel.screenState.collectAsState().value

    FruitAlbumScreen(
        screenState,
        viewModel::action,
        viewModel.effect
    )
}