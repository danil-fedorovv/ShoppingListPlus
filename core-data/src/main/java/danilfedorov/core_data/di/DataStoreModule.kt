package danilfedorov.core_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import danilfedorov.core_data.local.datasource.datastore.AppearanceDataStore
import danilfedorov.core_data.local.datasource.datastore.FruitAlbumDataStore
import danilfedorov.core_data.local.datasource.datastore.PremiumDataStore
import danilfedorov.core_data.repository.FruitAlbumRepositoryImpl
import danilfedorov.domain_core.repository.FruitAlbumRepository
import ru.rustore.sdk.pay.PurchaseInteractor
import ru.rustore.sdk.pay.RuStorePayClient
import javax.inject.Singleton

@Module
object DataStoreModule {

    @Provides
    @Singleton
    fun providePurchaseInteractor(): PurchaseInteractor =
        RuStorePayClient.instance.getPurchaseInteractor()

    @Provides
    @Singleton
    fun providePremiumDataStore(
        context: Context, purchaseInteractor: PurchaseInteractor
    ): PremiumDataStore = PremiumDataStore(context, purchaseInteractor)

    @Provides
    @Singleton
    fun provideAppearanceDataStore(context: Context): AppearanceDataStore =
        AppearanceDataStore(context)

    @Provides
    @Singleton
    fun provideFruitAlbumDataStore(context: Context): FruitAlbumDataStore {
        return FruitAlbumDataStore(context)
    }

    @Provides
    @Singleton
    fun provideFruitAlbumRepository(fruitAlbumDataStore: FruitAlbumDataStore): FruitAlbumRepository {
        return FruitAlbumRepositoryImpl(fruitAlbumDataStore)
    }

}