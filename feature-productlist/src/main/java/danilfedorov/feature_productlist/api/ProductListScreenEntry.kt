package danilfedorov.feature_productlist.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_productlist.internal.presenter.screen.ProductListScreen
import danilfedorov.feature_productlist.internal.presenter.viewmodel.ProductsViewModel
import danilfedorov.feature_productlist.internal.presenter.viewmodel.ProductsViewModelFactory

@Composable
fun ProductListScreenEntry(
    id: Long,
    addedCount: Int?,
    onNavigateHome : () -> Unit,
    navAddProduct: (Long, String?) -> Unit,
) {
    val deps = (LocalContext.current.applicationContext as ProductListDepsProvider).productListDeps
    val viewModel: ProductsViewModel = daggerViewModel {
        ProductsViewModelFactory(id, addedCount, onNavigateHome,navAddProduct, deps)
    }

    val screenState = viewModel.screenState.collectAsState()
    val sharedScreenState = viewModel.sharedState.collectAsState()

    ProductListScreen(
        screenState = screenState.value,
        sharedScreenState = sharedScreenState.value,
        action = viewModel::action,
        sharedAction = viewModel::sharedAction,
        effect = viewModel.effect
    )
}