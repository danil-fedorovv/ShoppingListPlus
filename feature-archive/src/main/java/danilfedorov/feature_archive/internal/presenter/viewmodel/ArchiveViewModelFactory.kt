package danilfedorov.feature_archive.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_archive.api.ArchiveDeps
import danilfedorov.feature_archive.internal.di.DaggerArchiveComponent

class ArchiveViewModelFactory(
    private val navHome: () -> Unit,
    private val deps: ArchiveDeps,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = DaggerArchiveComponent.factory().create(deps)

        @Suppress("UNCHECKED_CAST")
        return component.archiveViewModelFactory.create(
            navHome = navHome,
        ) as T
    }
}