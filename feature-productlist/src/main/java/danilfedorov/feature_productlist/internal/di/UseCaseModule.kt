package danilfedorov.feature_productlist.internal.di

import dagger.Module
import dagger.Provides
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.FruitAlbumRepository
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
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

@Module
internal object UseCaseModule {

    @Provides
    @FeatureScope
    fun provideInsertProductUseCase(repository: ProductRepository): InsertProductUseCase =
        InsertProductUseCase(repository)

    @Provides
    @FeatureScope
    fun provideObserveProductListByListIdUseCase(repository: ProductRepository): ObserveProductListByListIdUseCase =
        ObserveProductListByListIdUseCase(repository)

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
    fun provideChangeSortByAlphabetUseCase(repository: ShoppingListRepository): ChangeSortByAlphabetUseCase =
        ChangeSortByAlphabetUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetShoppingListsUseCase(repository: ShoppingListRepository): GetShoppingListsUseCase =
        GetShoppingListsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetShoppingListProgressUseCase(repository: ProductRepository): GetShoppingListProgressUseCase =
        GetShoppingListProgressUseCase(repository)

    @Provides
    @FeatureScope
    fun provideCreateListUseCase(repository: ShoppingListRepository): CreateListUseCase =
        CreateListUseCase(repository)

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
    fun provideObserveShoppingListByIdUseCase(
        repository: ShoppingListRepository
    ): ObserveShoppingListByIdUseCase =
        ObserveShoppingListByIdUseCase(repository)

    @Provides
    @FeatureScope
    fun provideObserveRemoteProductChangesUseCase(
        repository: ProductRepository
    ): ObserveRemoteProductChangesUseCase =
        ObserveRemoteProductChangesUseCase(repository)

    @Provides
    @FeatureScope
    fun provideObserveConnectionStatusUseCase(
        repository: ProductRepository
    ): ObserveConnectionStatusUseCase =
        ObserveConnectionStatusUseCase(repository)

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


