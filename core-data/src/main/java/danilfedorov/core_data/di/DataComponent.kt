package danilfedorov.core_data.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import danilfedorov.domain_core.repository.AppearanceRepository
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ProductSuggestionRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, MapperModule::class,
    DataStoreModule::class, RepositoryModule::class])
interface DataComponent {

    val productRepository: ProductRepository
    val shoppingListRepository: ShoppingListRepository
    val appearanceRepository: AppearanceRepository
    val suggestionRepository: ProductSuggestionRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DataComponent
    }
}