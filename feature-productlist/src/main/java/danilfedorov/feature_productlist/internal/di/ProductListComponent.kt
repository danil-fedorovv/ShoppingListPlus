package danilfedorov.feature_productlist.internal.di

import android.content.res.Resources
import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.fruitalbum.GetRandomActiveUnlockedFruitUseCase
import danilfedorov.domain_core.usecase.product.DeleteProductsUseCase
import danilfedorov.domain_core.usecase.product.GetShoppingListProgressUseCase
import danilfedorov.domain_core.usecase.product.InsertProductUseCase
import danilfedorov.domain_core.usecase.product.ObserveConnectionStatusUseCase
import danilfedorov.domain_core.usecase.product.ObserveProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.ObserveRemoteProductChangesUseCase
import danilfedorov.domain_core.usecase.product.SelectAllProductsUseCase
import danilfedorov.domain_core.usecase.product.UnselectAllProductsUseCase
import danilfedorov.domain_core.usecase.product.UpdateProductCardUseCase
import danilfedorov.domain_core.usecase.product.UpdateProductSelectionUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ChangeSortByAlphabetUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveRemoteListNameUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveShoppingListByIdUseCase
import danilfedorov.domain_core.usecase.shoppinglist.PushListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import danilfedorov.feature_productlist.api.ProductListDeps


@Component(
    dependencies = [ProductListDeps::class],
    modules = [UseCaseModule::class]
)
@FeatureScope
internal interface ProductListComponent {

    val renameShoppingListUseCase: RenameShoppingListUseCase
    val insertProductUseCase: InsertProductUseCase
    val observeProductListByListIdUseCase: ObserveProductListByListIdUseCase
    val updateProductUseCase: UpdateProductSelectionUseCase
    val selectAllProductsUseCase: SelectAllProductsUseCase
    val unselectAllProductsUseCase: UnselectAllProductsUseCase
    val updateProductCardUseCase: UpdateProductCardUseCase
    val deleteProductsUseCase: DeleteProductsUseCase
    val observeShoppingListByIdUseCase: ObserveShoppingListByIdUseCase
    val changeSortByAlphabetUseCase: ChangeSortByAlphabetUseCase
    val getShoppingListProgressUseCase: GetShoppingListProgressUseCase
    val createListUseCase: CreateListUseCase
    val pushListUseCase: PushListUseCase
    val getShoppingListsUseCase: GetShoppingListsUseCase
    val observeRemoteProductChangesUseCase: ObserveRemoteProductChangesUseCase
    val observeConnectionStatusUseCase: ObserveConnectionStatusUseCase
    val observeRemoteListNameUseCase: ObserveRemoteListNameUseCase
    val getRandomActiveUnlockedFruitUseCase: GetRandomActiveUnlockedFruitUseCase
    val resources: Resources

    @Component.Factory
    interface Factory {
        fun create(deps: ProductListDeps): ProductListComponent
    }
}