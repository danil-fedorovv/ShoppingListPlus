package danilfedorov.core_data.di

import dagger.Module
import dagger.Provides
import danilfedorov.core_data.datasource.datastore.AppearanceDataStore
import danilfedorov.core_data.datasource.room.dao.ProductDao
import danilfedorov.core_data.datasource.room.dao.ShoppingListDao
import danilfedorov.core_data.datasource.room.mapper.ProductMapper
import danilfedorov.core_data.datasource.room.mapper.ShoppingListMapper
import danilfedorov.core_data.impl.AppearanceRepositoryImpl
import danilfedorov.core_data.impl.ProductRepositoryImpl
import danilfedorov.core_data.impl.ShoppingListRepositoryImpl
import danilfedorov.domain_core.repository.AppearanceRepository
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
import javax.inject.Singleton

@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAppearanceRepository(appearanceDataStore: AppearanceDataStore): AppearanceRepository {
        return AppearanceRepositoryImpl(appearanceDataStore)
    }

    @Provides
    @Singleton
    fun provideProductRepository(dao: ProductDao, mapper: ProductMapper): ProductRepository {
        return ProductRepositoryImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideShoppingListRepository(
        dao: ShoppingListDao,
        mapper: ShoppingListMapper
    ): ShoppingListRepository {
        return ShoppingListRepositoryImpl(dao, mapper)
    }
}