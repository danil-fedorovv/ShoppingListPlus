package danilfedorov.shoppinglistplus

import android.app.Application
import danilfedorov.core_data.di.DaggerDataComponent
import danilfedorov.feature_archive.api.ArchiveDeps
import danilfedorov.feature_archive.api.ArchiveDepsProvider
import danilfedorov.feature_settings.api.SettingsDeps
import danilfedorov.feature_settings.api.SettingsDepsProvider
import danilfedorov.feature_shoppinglist.api.ShoppingListDeps
import danilfedorov.feature_shoppinglist.api.ShoppingListDepsProvider
import danilfedorov.feature_productlist.api.ProductListDeps
import danilfedorov.feature_productlist.api.ProductListDepsProvider
import danilfedorov.shoppinglistplus.di.AppComponent
import danilfedorov.shoppinglistplus.di.DaggerAppComponent

class ShoppingListPlusApplication() : Application(), ShoppingListDepsProvider, ProductListDepsProvider, SettingsDepsProvider, ArchiveDepsProvider {
    lateinit var appComponent: AppComponent

    private val deps: AppComponent
        get() = appComponent

    override val shoppingListDeps: ShoppingListDeps get() = deps
    override val productListDeps: ProductListDeps get() = deps
    override val settingsDeps: SettingsDeps get() = deps
    override val archiveDeps: ArchiveDeps get() = deps

    override fun onCreate() {
        super.onCreate()

        val coreDataComponent = DaggerDataComponent.factory()
            .create(this)

        appComponent = DaggerAppComponent.factory()
            .create(this, coreDataComponent)
    }

}