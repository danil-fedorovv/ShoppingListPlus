package danilfedorov.feature_archive.internal.di

import android.content.res.Resources
import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.shoppinglist.DeleteListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveArchivedShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RestoreShoppingListUseCase
import danilfedorov.feature_archive.api.ArchiveDeps
import danilfedorov.feature_archive.internal.presenter.viewmodel.ArchiveViewModel

@FeatureScope
@Component(dependencies = [ArchiveDeps::class])
interface ArchiveComponent {

    val archiveViewModelFactory: ArchiveViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(deps: ArchiveDeps): ArchiveComponent
    }
}