package danilfedorov.shoppinglistplus.di

import android.content.Context
import android.content.res.Resources
import dagger.BindsInstance
import dagger.Component
import danilfedorov.common.di.AppScope
import danilfedorov.common.di.CommonModule
import danilfedorov.core_data.di.DataComponent
import danilfedorov.domain_core.repository.AppearanceRepository
import danilfedorov.domain_core.repository.PremiumRepository
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ProductSuggestionRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
import danilfedorov.feature_add_product.api.AddProductDeps
import danilfedorov.feature_archive.api.ArchiveDeps
import danilfedorov.feature_fruit_album.api.FruitAlbumDeps
import danilfedorov.feature_premium.api.PremiumDeps
import danilfedorov.feature_productlist.api.ProductListDeps
import danilfedorov.feature_settings.api.SettingsDeps
import danilfedorov.feature_shoppinglist.api.ShoppingListDeps
import danilfedorov.shoppinglistplus.BuildConfig
import danilfedorov.shoppinglistplus.ShoppingListPlusApplication

@AppScope
@Component(dependencies = [DataComponent::class], modules = [CommonModule::class])
interface AppComponent : ShoppingListDeps, ProductListDeps, SettingsDeps, ArchiveDeps,
    PremiumDeps, FruitAlbumDeps, AddProductDeps {

    override val shoppingListRepository: ShoppingListRepository
    override val productRepository: ProductRepository
    override val appearanceRepository: AppearanceRepository
    override val premiumRepository: PremiumRepository
    override val productSuggestionRepository: ProductSuggestionRepository
    override val yandexRewardedKey: String
        get() = BuildConfig.YANDEX_REWARDED_KEY
    override val res: Resources

    fun inject(app: ShoppingListPlusApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, dataComponent: DataComponent): AppComponent
    }
}