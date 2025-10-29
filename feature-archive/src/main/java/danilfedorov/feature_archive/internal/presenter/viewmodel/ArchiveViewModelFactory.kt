package danilfedorov.feature_archive.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_archive.api.ArchiveDeps
import danilfedorov.feature_archive.internal.di.DaggerArchiveComponent

class ArchiveViewModelFactory(
    val navHome: () -> Unit,
    val deps: ArchiveDeps
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = DaggerArchiveComponent.factory().create(deps)

        return ArchiveViewModel(
            navHome,
            component.getArchivedShoppingListsUseCase,
            component.getSelectedProductCountUseCase,
            component.getTotalProductCountUseCase,
            component.deleteListsUseCase,
            component.getProductListByListIdUseCase,
            component.restoreShoppingListUseCase,
            component.res
        ) as T
    }
}