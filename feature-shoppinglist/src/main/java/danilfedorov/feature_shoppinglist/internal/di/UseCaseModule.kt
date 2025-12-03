package danilfedorov.feature_shoppinglist.internal.di

import dagger.Module
import dagger.Provides
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.FruitAlbumRepository
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
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


@Module
internal object UseCaseModule {

    @Provides
    @FeatureScope
    fun provideCreateListUseCase(repository: ShoppingListRepository): CreateListUseCase =
        CreateListUseCase(repository)

    @Provides
    @FeatureScope
    fun provideInsertProductsUseCase(repository: ProductRepository): InsertProductsUseCase =
        InsertProductsUseCase(repository)

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
    fun provideFetchListUseCase(
        shoppingListRepository: ShoppingListRepository,
        productRepository: ProductRepository
    ): FetchListUseCase =
        FetchListUseCase(
            shoppingListRepository, productRepository
        )

    @Provides
    @FeatureScope
    fun providePushListUseCase(
        shoppingListRepository: ShoppingListRepository,
    ): PushListUseCase =
        PushListUseCase(
            shoppingListRepository
        )


    @Provides
    @FeatureScope
    fun provideArchiveShoppingListsUseCase(repository: ShoppingListRepository): ArchiveShoppingListsUseCase =
        ArchiveShoppingListsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideObserveShoppingListsUseCase(
        repository: ShoppingListRepository
    ): ObserveShoppingListsUseCase =
        ObserveShoppingListsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetRemoteShoppingListNameUseCase(
        repository: ShoppingListRepository
    ): GetRemoteShoppingListNameUseCase =
        GetRemoteShoppingListNameUseCase(repository)

    @Provides
    @FeatureScope
    fun provideObserveRemoteListNameUseCase(
        repository: ShoppingListRepository
    ): ObserveRemoteListNameUseCase =
        ObserveRemoteListNameUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetRandomActiveUnlockedFruitUseCase(
        repository: FruitAlbumRepository
    ): GetRandomActiveUnlockedFruitUseCase =
        GetRandomActiveUnlockedFruitUseCase(repository)

}