package danilfedorov.feature_shoppinglist.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_shoppinglist.internal.presenter.screen.ShoppingListScreen
import danilfedorov.feature_shoppinglist.internal.presenter.viewmodel.ShoppingListViewModel
import danilfedorov.feature_shoppinglist.internal.presenter.viewmodel.ShoppingListViewModelFactory

@Composable
fun ShoppingListScreenEntry(
    navArchive: () -> Unit,
    navSettings: () -> Unit,
    navProduct: (id: Long) -> Unit
) {
    val deps =
        (LocalContext.current.applicationContext as ShoppingListDepsProvider).shoppingListDeps
    val viewModel: ShoppingListViewModel = daggerViewModel {
        ShoppingListViewModelFactory(navArchive, navSettings, navProduct, deps)
    }
    val screenState = viewModel.screenState.collectAsState()

    ShoppingListScreen(
        screenState = screenState.value,
        action = { viewModel.action(it) },
        effect = viewModel.effect
    )
}
