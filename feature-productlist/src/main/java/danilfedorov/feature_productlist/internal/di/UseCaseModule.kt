package danilfedorov.feature_productlist.internal.di

import dagger.Module
import dagger.Provides
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
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

@Module
internal object UseCaseModule {

    @Provides
    @FeatureScope
    fun provideCreateProductUseCase(repository: ProductRepository): CreateProductUseCase =
        CreateProductUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetProductsByListIdUseCase(repository: ProductRepository): GetProductListByListIdUseCase =
        GetProductListByListIdUseCase(repository)

    @Provides
    @FeatureScope
    fun provideUpdateProductSelectionUseCase(repository: ProductRepository): UpdateProductSelectionUseCase =
        UpdateProductSelectionUseCase(repository)

    @Provides
    @FeatureScope
    fun provideSelectAllProductsUseCase(repository: ProductRepository): SelectAllProductsUseCase =
        SelectAllProductsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideUnselectAllProductsUseCase(repository: ProductRepository): UnselectAllProductsUseCase =
        UnselectAllProductsUseCase(repository)


    @Provides
    @FeatureScope
    fun provideUpdateProductCardUseCase(repository: ProductRepository): UpdateProductCardUseCase =
        UpdateProductCardUseCase(repository)

    @Provides
    @FeatureScope
    fun provideDeleteProductsUseCase(repository: ProductRepository): DeleteProductsUseCase =
        DeleteProductsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideRenameShoppingListUseCase(repository: ShoppingListRepository): RenameShoppingListUseCase =
        RenameShoppingListUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetShoppingListByIdUseCase(repository: ShoppingListRepository): GetShoppingListByIdUseCase =
        GetShoppingListByIdUseCase(repository)

    @Provides
    @FeatureScope
    fun provideChangeSortByAlphabetUseCase(repository: ShoppingListRepository): ChangeSortByAlphabetUseCase =
        ChangeSortByAlphabetUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetListsUseCase(repository: ShoppingListRepository): GetListsUseCase =
        GetListsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetShoppingListProgressUseCase(repository: ProductRepository): GetShoppingListProgressUseCase =
        GetShoppingListProgressUseCase(repository)

    @Provides
    @FeatureScope
    fun provideCreateListUseCase(repository: ShoppingListRepository): CreateListUseCase =
        CreateListUseCase(repository)
}
