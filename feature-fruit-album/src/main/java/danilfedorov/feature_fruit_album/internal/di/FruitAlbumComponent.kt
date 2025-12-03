package danilfedorov.feature_fruit_album.internal.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import danilfedorov.common.ad.AdManager
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.usecase.fruitalbum.GetFruitFlowUseCase
import danilfedorov.domain_core.usecase.fruitalbum.ToggleFruitEnabledUseCase
import danilfedorov.domain_core.usecase.fruitalbum.UnlockRandomFruitUseCase
import danilfedorov.feature_fruit_album.api.FruitAlbumDeps

@FeatureScope
@Component(dependencies = [FruitAlbumDeps::class], modules = [UseCaseModule::class])
interface FruitAlbumComponent {

    val getFruitFlowUseCase: GetFruitFlowUseCase
    val unlockRandomFruitUseCase: UnlockRandomFruitUseCase
    val toggleFruitEnabledUseCase: ToggleFruitEnabledUseCase
    val adManager: AdManager

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, deps: FruitAlbumDeps): FruitAlbumComponent
    }
}