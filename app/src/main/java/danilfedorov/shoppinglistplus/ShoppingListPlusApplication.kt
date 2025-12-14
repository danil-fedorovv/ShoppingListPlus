package danilfedorov.shoppinglistplus

import android.app.Application
import com.google.firebase.FirebaseApp
import danilfedorov.core_data.di.DaggerDataComponent
import danilfedorov.feature_add_product.api.AddProductDeps
import danilfedorov.feature_add_product.api.AddProductDepsProvider
import danilfedorov.feature_archive.api.ArchiveDeps
import danilfedorov.feature_archive.api.ArchiveDepsProvider
import danilfedorov.feature_fruit_album.api.FruitAlbumDeps
import danilfedorov.feature_fruit_album.api.FruitAlbumDepsProvider
import danilfedorov.feature_premium.api.PremiumDeps
import danilfedorov.feature_premium.api.PremiumDepsProvider
import danilfedorov.feature_productlist.api.ProductListDeps
import danilfedorov.feature_productlist.api.ProductListDepsProvider
import danilfedorov.feature_settings.api.SettingsDeps
import danilfedorov.feature_settings.api.SettingsDepsProvider
import danilfedorov.feature_shoppinglist.api.ShoppingListDeps
import danilfedorov.feature_shoppinglist.api.ShoppingListDepsProvider
import danilfedorov.shoppinglistplus.di.AppComponent
import danilfedorov.shoppinglistplus.di.DaggerAppComponent

class ShoppingListPlusApplication() : Application(), ShoppingListDepsProvider, ProductListDepsProvider,
    SettingsDepsProvider, ArchiveDepsProvider, PremiumDepsProvider, FruitAlbumDepsProvider,
    AddProductDepsProvider{
    lateinit var appComponent: AppComponent

    private val deps: AppComponent
        get() = appComponent

    override val shoppingListDeps: ShoppingListDeps get() = deps
    override val productListDeps: ProductListDeps get() = deps
    override val settingsDeps: SettingsDeps get() = deps
    override val archiveDeps: ArchiveDeps get() = deps
    override val premiumDeps: PremiumDeps get() = deps
    override val fruitAlbumDeps: FruitAlbumDeps get() = deps
    override val addProductDeps: AddProductDeps get() = deps

    override fun onCreate() {
        super.onCreate()

        val coreDataComponent = DaggerDataComponent.factory()
            .create(this)

        appComponent = DaggerAppComponent.factory()
            .create(this, coreDataComponent)

        FirebaseApp.initializeApp(this)
    }

}