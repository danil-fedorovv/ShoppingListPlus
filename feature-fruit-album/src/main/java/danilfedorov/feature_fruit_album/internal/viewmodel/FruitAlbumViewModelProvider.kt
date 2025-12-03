package danilfedorov.feature_fruit_album.internal.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danilfedorov.feature_fruit_album.api.FruitAlbumDeps
import danilfedorov.feature_fruit_album.internal.di.DaggerFruitAlbumComponent

class FruitAlbumViewModelProvider(
    private val deps: FruitAlbumDeps,
    private val navHome: () -> Unit,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val component = DaggerFruitAlbumComponent.factory().create(context, deps)

        return FruitAlbumViewModel(
            navHome,
            component.getFruitFlowUseCase,
            component.unlockRandomFruitUseCase,
            component.toggleFruitEnabledUseCase,
            component.adManager
        ) as T
    }
}