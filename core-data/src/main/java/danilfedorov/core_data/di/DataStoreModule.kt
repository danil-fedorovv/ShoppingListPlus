package danilfedorov.core_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import danilfedorov.core_data.local.datasource.datastore.AppearanceDataStore
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
    fun provideAppearanceDataStore(context: Context): AppearanceDataStore =
        AppearanceDataStore(context)

}