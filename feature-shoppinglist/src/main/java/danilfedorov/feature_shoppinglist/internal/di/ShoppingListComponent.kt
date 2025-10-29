package danilfedorov.feature_shoppinglist.internal.di

import android.content.res.Resources
import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.product.CreateProductUseCase
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ArchiveShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.DeleteListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import danilfedorov.feature_shoppinglist.api.ShoppingListDeps

@FeatureScope
@Component(
    dependencies = [ShoppingListDeps::class],
    modules = [UseCaseModule::class]
)
internal interface ShoppingListComponent {

    val getListsUseCase: GetListsUseCase
    val createListUseCase: CreateListUseCase
    val deleteListsUseCase: DeleteListsUseCase
    val renameShoppingListUseCase: RenameShoppingListUseCase
    val getSelectedProductCountUseCase: GetSelectedProductCountUseCase
    val getTotalProductCountUseCase: GetTotalProductCountUseCase
    val getProductListByListIdUseCase: GetProductListByListIdUseCase
    val createProductUseCase: CreateProductUseCase
    val archiveShoppingListsUseCase: ArchiveShoppingListsUseCase
    val res: Resources

    @Component.Factory
    interface Factory {

        fun create(deps: ShoppingListDeps): ShoppingListComponent

    }
}