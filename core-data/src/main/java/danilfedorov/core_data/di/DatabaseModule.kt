package danilfedorov.core_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import danilfedorov.core_data.local.datasource.room.dao.ProductDao
import danilfedorov.core_data.local.datasource.room.dao.ProductSuggestionDao
import danilfedorov.core_data.local.datasource.room.dao.ShoppingListDao
import danilfedorov.core_data.local.datasource.room.database.ShoppingListDatabase
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProductDao(database: ShoppingListDatabase): ProductDao =
        database.getShoppingItemDao()

    @Provides
    @Singleton
    fun provideProductSuggestionDao(database: ShoppingListDatabase): ProductSuggestionDao =
        database.getProductSuggestionDao()

    @Provides
    @Singleton
    fun provideShoppingListDao(database: ShoppingListDatabase): ShoppingListDao =
        database.getShoppingListDao()

    @Provides
    @Singleton
    fun provideShoppingListDatabase(context: Context): ShoppingListDatabase =
        ShoppingListDatabase.getDataBase(context)
}