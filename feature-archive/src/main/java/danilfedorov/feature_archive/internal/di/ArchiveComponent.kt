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

@FeatureScope
@Component(
    dependencies = [ArchiveDeps::class],
    modules = [UseCaseModule::class]
)
interface ArchiveComponent {
    val observeArchivedShoppingListsUseCase: ObserveArchivedShoppingListsUseCase
    val getSelectedProductCountUseCase: GetSelectedProductCountUseCase
    val getTotalProductCountUseCase: GetTotalProductCountUseCase
    val deleteListsUseCase: DeleteListsUseCase
    val getProductListByListIdUseCase: GetProductListByListIdUseCase
    val restoreShoppingListUseCase: RestoreShoppingListUseCase
    val res: Resources

    @Component.Factory
    interface Factory {
        fun create(deps: ArchiveDeps): ArchiveComponent
    }
}