package danilfedorov.feature_shoppinglist.internal.di

import android.content.res.Resources
import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.fruitalbum.GetRandomActiveUnlockedFruitUseCase
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.product.InsertProductsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ArchiveShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.DeleteListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.FetchListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetRemoteShoppingListNameUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveRemoteListNameUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.PushListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import danilfedorov.feature_shoppinglist.api.ShoppingListDeps

@FeatureScope
@Component(
    dependencies = [ShoppingListDeps::class],
    modules = [UseCaseModule::class]
)
internal interface ShoppingListComponent {

    val createListUseCase: CreateListUseCase
    val deleteListsUseCase: DeleteListsUseCase
    val renameShoppingListUseCase: RenameShoppingListUseCase
    val getSelectedProductCountUseCase: GetSelectedProductCountUseCase
    val getTotalProductCountUseCase: GetTotalProductCountUseCase
    val getProductListByListIdUseCase: GetProductListByListIdUseCase
    val insertProductsUseCase: InsertProductsUseCase
    val archiveShoppingListsUseCase: ArchiveShoppingListsUseCase
    val pushListUseCase: PushListUseCase
    val fetchListUseCase: FetchListUseCase
    val res: Resources
    val observeShoppingListsUseCase: ObserveShoppingListsUseCase
    val getRemoteShoppingListNameUseCase: GetRemoteShoppingListNameUseCase
    val observeRemoteListNameUseCase: ObserveRemoteListNameUseCase
    val getRandomActiveUnlockedFruitUseCase: GetRandomActiveUnlockedFruitUseCase

    @Component.Factory
    interface Factory {

        fun create(deps: ShoppingListDeps): ShoppingListComponent

    }
}