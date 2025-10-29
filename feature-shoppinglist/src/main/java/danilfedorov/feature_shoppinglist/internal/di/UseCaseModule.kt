package danilfedorov.feature_shoppinglist.internal.di

import dagger.Module
import dagger.Provides
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
import danilfedorov.domain_core.usecase.product.CreateProductUseCase
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ArchiveShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.DeleteListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase


@Module
internal object UseCaseModule {

    @Provides
    @FeatureScope
    fun provideCreateListUseCase(repository: ShoppingListRepository): CreateListUseCase =
        CreateListUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetListsUseCase(repository: ShoppingListRepository): GetListsUseCase =
        GetListsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideDeleteListsUseCase(repository: ShoppingListRepository): DeleteListsUseCase =
        DeleteListsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideRenameListUseCase(repository: ShoppingListRepository): RenameShoppingListUseCase =
        RenameShoppingListUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetSelectedProductCountUseCase(repository: ProductRepository): GetSelectedProductCountUseCase =
        GetSelectedProductCountUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetTotalProductCountUseCase(repository: ProductRepository): GetTotalProductCountUseCase =
        GetTotalProductCountUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetProductsByListIdUseCase(repository: ProductRepository): GetProductListByListIdUseCase =
        GetProductListByListIdUseCase(repository)

    @Provides
    @FeatureScope
    fun provideCreateProductUseCase(repository: ProductRepository): CreateProductUseCase =
        CreateProductUseCase(repository)


    @Provides
    @FeatureScope
    fun provideArchiveShoppingListsUseCase(repository: ShoppingListRepository): ArchiveShoppingListsUseCase =
        ArchiveShoppingListsUseCase(repository)



}