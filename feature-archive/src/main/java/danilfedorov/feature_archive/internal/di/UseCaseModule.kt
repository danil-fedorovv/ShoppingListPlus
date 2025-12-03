package danilfedorov.feature_archive.internal.di

import dagger.Module
import dagger.Provides
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.shoppinglist.DeleteListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveArchivedShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RestoreShoppingListUseCase

@Module
object UseCaseModule {
    @FeatureScope
    @Provides
    fun provideObserveArchivedShoppingListsUseCase(repository: ShoppingListRepository): ObserveArchivedShoppingListsUseCase =
        ObserveArchivedShoppingListsUseCase(repository)


    @FeatureScope
    @Provides
    fun provideDeleteListsUseCase(repository: ShoppingListRepository): DeleteListsUseCase =
        DeleteListsUseCase(repository)


    @FeatureScope
    @Provides
    fun provideRestoreShoppingListUseCase(repository: ShoppingListRepository): RestoreShoppingListUseCase =
        RestoreShoppingListUseCase(repository)


    @FeatureScope
    @Provides
    fun provideGetProductListByListIdUseCase(repository: ProductRepository): GetProductListByListIdUseCase =
        GetProductListByListIdUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetSelectedProductCountUseCase(repository: ProductRepository): GetSelectedProductCountUseCase =
        GetSelectedProductCountUseCase(repository)

    @Provides
    @FeatureScope
    fun provideGetTotalProductCountUseCase(repository: ProductRepository): GetTotalProductCountUseCase =
        GetTotalProductCountUseCase(repository)

}