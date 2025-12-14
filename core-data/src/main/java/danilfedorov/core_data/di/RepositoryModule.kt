package danilfedorov.core_data.di

import dagger.Module
import dagger.Provides
import danilfedorov.core_data.local.datasource.datastore.AppearanceDataStore
import danilfedorov.core_data.local.datasource.datastore.PremiumDataStore
import danilfedorov.core_data.local.datasource.room.dao.ProductDao
import danilfedorov.core_data.local.datasource.room.dao.ProductSuggestionDao
import danilfedorov.core_data.local.datasource.room.dao.ShoppingListDao
import danilfedorov.core_data.local.datasource.room.mapper.ProductMapper
import danilfedorov.core_data.local.datasource.room.mapper.ProductSuggestionMapper
import danilfedorov.core_data.local.datasource.room.mapper.ShoppingListMapper
import danilfedorov.core_data.remote.realtime.datasource.FirebaseRealtimeDataSource
import danilfedorov.core_data.repository.AppearanceRepositoryImpl
import danilfedorov.core_data.repository.PremiumRepositoryImpl
import danilfedorov.core_data.repository.ProductRepositoryImpl
import danilfedorov.core_data.repository.ProductSuggestionRepositoryImpl
import danilfedorov.core_data.repository.ShoppingListRepositoryImpl
import danilfedorov.domain_core.repository.AppearanceRepository
import danilfedorov.domain_core.repository.FruitAlbumRepository
import danilfedorov.domain_core.repository.PremiumRepository
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ProductSuggestionRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseDataSource(): FirebaseRealtimeDataSource {
        return FirebaseRealtimeDataSource()
    }

    @Provides
    @Singleton
    fun providePremiumRepository(
        premiumDataStore: PremiumDataStore,
        fruitAlbumRepository: FruitAlbumRepository,
    ): PremiumRepository {
        return PremiumRepositoryImpl(premiumDataStore, fruitAlbumRepository)
    }

    @Provides
    @Singleton
    fun provideAppearanceRepository(appearanceDataStore: AppearanceDataStore): AppearanceRepository {
        return AppearanceRepositoryImpl(appearanceDataStore)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        dao: ProductDao,
        mapper: ProductMapper,
        firebaseRealtimeDataSource: FirebaseRealtimeDataSource,
    ): ProductRepository {
        return ProductRepositoryImpl(dao, mapper, firebaseRealtimeDataSource)
    }

    @Provides
    @Singleton
    fun provideShoppingListRepository(
        dao: ShoppingListDao,
        mapper: ShoppingListMapper,
        firebaseRealtimeDataSource: FirebaseRealtimeDataSource,
    ): ShoppingListRepository {
        return ShoppingListRepositoryImpl(dao, mapper, firebaseRealtimeDataSource)
    }

    @Provides
    @Singleton
    fun provideProductSuggestionRepository(
        dao: ProductSuggestionDao,
        mapper: ProductSuggestionMapper
    ): ProductSuggestionRepository {
        return ProductSuggestionRepositoryImpl(dao, mapper)
    }

}