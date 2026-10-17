package danilfedorov.core_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import danilfedorov.core_data.local.datasource.datastore.AppearanceDataStore
import javax.inject.Singleton

@Module
object DataStoreModule {
    @Provides
    @Singleton
    fun provideAppearanceDataStore(context: Context): AppearanceDataStore =
        AppearanceDataStore(context)

}