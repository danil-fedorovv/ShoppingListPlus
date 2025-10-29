package danilfedorov.core_data.di

import dagger.Module
import dagger.Provides
import danilfedorov.core_data.datasource.room.mapper.ProductMapper
import danilfedorov.core_data.datasource.room.mapper.ShoppingListMapper
import javax.inject.Singleton

@Module
object MapperModule {

    @Provides
    @Singleton
    fun provideProductMapper(): ProductMapper =
        ProductMapper()

    @Provides
    @Singleton
    fun provideShoppingListMapper(): ShoppingListMapper =
        ShoppingListMapper()
}