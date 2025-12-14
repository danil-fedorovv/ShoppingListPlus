package danilfedorov.feature_add_product.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import danilfedorov.common.compose.util.daggerViewModel
import danilfedorov.feature_add_product.internal.presenter.screen.AddProductScreen
import danilfedorov.feature_add_product.internal.presenter.viewmodel.AddProductViewModel
import danilfedorov.feature_add_product.internal.presenter.viewmodel.AddProductViewModelFactory

@Composable
fun AddProductScreenEntry(
    shoppingListId: Long,
    remoteListId: String?,
    navBack: (Long, Int?) -> Unit
) {

    val deps = (LocalContext.current.applicationContext as AddProductDepsProvider).addProductDeps
    val viewModel = daggerViewModel<AddProductViewModel> {
        AddProductViewModelFactory(shoppingListId, remoteListId, navBack, deps)
    }

    val screenState = viewModel.screenState.collectAsState().value

    AddProductScreen(
        screenState = screenState,
        action = viewModel::action
    )

}