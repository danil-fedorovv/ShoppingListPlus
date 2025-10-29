package danilfedorov.common.compose.util

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VM : ViewModel> daggerViewModel(
    crossinline factoryProvider: () -> ViewModelProvider.Factory
): VM {
    val owner = LocalViewModelStoreOwner.current
        ?: error("No ViewModelStoreOwner was provided")
    return viewModel(
        modelClass = VM::class.java,
        factory = factoryProvider(),
        viewModelStoreOwner = owner
    )
}