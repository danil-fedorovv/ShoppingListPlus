package danilfedorov.feature_productlist.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_productlist.internal.presenter.screen.ProductListScreen
import danilfedorov.feature_productlist.internal.presenter.viewmodel.ProductListViewModel
import danilfedorov.feature_productlist.internal.presenter.viewmodel.ProductListViewModelFactory

@Composable
fun ProductListScreenEntry(
    id: Long,
    onNavigateHome : () -> Unit 
) {
    val deps = (LocalContext.current.applicationContext as ProductListDepsProvider).productListDeps
    val viewModel: ProductListViewModel = daggerViewModel {
        ProductListViewModelFactory(id, onNavigateHome, deps)
    }

    val screenState = viewModel.screenState.collectAsState()

    ProductListScreen(
        screenState = screenState.value,
        action = { viewModel.action(it) },
        effect = viewModel.effect
    )
}