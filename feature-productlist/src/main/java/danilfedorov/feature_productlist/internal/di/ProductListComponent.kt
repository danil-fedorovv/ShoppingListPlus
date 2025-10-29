package danilfedorov.feature_productlist.internal.di

import android.content.res.Resources
import dagger.Component
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.product.CreateProductUseCase
import danilfedorov.domain_core.usecase.product.DeleteProductsUseCase
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetShoppingListProgressUseCase
import danilfedorov.domain_core.usecase.product.SelectAllProductsUseCase
import danilfedorov.domain_core.usecase.product.UnselectAllProductsUseCase
import danilfedorov.domain_core.usecase.product.UpdateProductCardUseCase
import danilfedorov.domain_core.usecase.product.UpdateProductSelectionUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ChangeSortByAlphabetUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetShoppingListByIdUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import danilfedorov.feature_productlist.api.ProductListDeps


@Component(
    dependencies = [ProductListDeps::class],
    modules = [UseCaseModule::class]
)
@FeatureScope
internal interface ProductListComponent {

    val renameShoppingListUseCase: RenameShoppingListUseCase
    val getProductListByListIdUseCase: GetProductListByListIdUseCase
    val createProductUseCase: CreateProductUseCase
    val updateProductUseCase: UpdateProductSelectionUseCase
    val selectAllProductsUseCase: SelectAllProductsUseCase
    val unselectAllProductsUseCase: UnselectAllProductsUseCase
    val updateProductCardUseCase: UpdateProductCardUseCase
    val deleteProductsUseCase: DeleteProductsUseCase
    val getShoppingListByIdUseCase: GetShoppingListByIdUseCase
    val changeSortByAlphabetUseCase: ChangeSortByAlphabetUseCase
    val getListsUseCase: GetListsUseCase
    val getShoppingListProgressUseCase: GetShoppingListProgressUseCase
    val createListUseCase: CreateListUseCase
    val resources: Resources

    @Component.Factory
    interface Factory {
        fun create(deps: ProductListDeps): ProductListComponent
    }
}