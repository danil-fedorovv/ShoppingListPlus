package danilfedorov.feature_shoppinglist.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.common.deeplink.DeepLink
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.Action
import danilfedorov.feature_shoppinglist.internal.presenter.screen.ShoppingListScreen
import danilfedorov.feature_shoppinglist.internal.presenter.viewmodel.ShoppingListViewModel
import danilfedorov.feature_shoppinglist.internal.presenter.viewmodel.ShoppingListViewModelFactory

@Composable
fun ShoppingListScreenEntry(
    deepLink: DeepLink?,
    nullDeepLink: () -> Unit,
    navArchive: () -> Unit,
    navSettings: () -> Unit,
    navProduct: (id: Long) -> Unit,
) {
    val deps =
        (LocalContext.current.applicationContext as ShoppingListDepsProvider).shoppingListDeps
    val viewModel: ShoppingListViewModel = daggerViewModel {
        ShoppingListViewModelFactory(navArchive, navSettings, navProduct, deps)
    }

    LaunchedEffect(deepLink) {
        deepLink?.let { viewModel.action(Action.HandleDeepLink(it)) }
        nullDeepLink()
    }

    val screenState = viewModel.screenState.collectAsState()
    val sharedScreenState = viewModel.sharedState.collectAsState()

    ShoppingListScreen(
        screenState = screenState.value,
        sharedScreenState = sharedScreenState.value,
        action = viewModel::action,
        sharedAction = viewModel::sharedAction,
        effect = viewModel.effect
    )
}
