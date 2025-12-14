package danilfedorov.feature_fruit_album.internal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.common.ad.AdManager
import danilfedorov.domain_core.usecase.fruitalbum.GetFruitFlowUseCase
import danilfedorov.domain_core.usecase.fruitalbum.ToggleFruitEnabledUseCase
import danilfedorov.domain_core.usecase.fruitalbum.UnlockRandomFruitUseCase
import danilfedorov.feature_fruit_album.internal.mvi.Action
import danilfedorov.feature_fruit_album.internal.mvi.Effect
import danilfedorov.feature_fruit_album.internal.mvi.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FruitAlbumViewModel(
    private val navHome: () -> Unit,
    private val getFruitFlowUseCase: GetFruitFlowUseCase,
    private val unlockRandomFruitUseCase: UnlockRandomFruitUseCase,
    private val toggleFruitEnabledUseCase: ToggleFruitEnabledUseCase,
    private val adManager: AdManager,
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            getFruitFlowUseCase().collect { fruitAlbum ->
                _screenState.update { it.copy(fruitAlbum = fruitAlbum) }
            }
        }
    }

    fun action(action: Action) {
        when (action) {
            Action.NavHome -> navHome()
            is Action.ShowAd -> showAd()
            is Action.ToggleEnabled -> toggleEnabled(action.index)
            is Action.HideUnlockFruitSheet -> hideUnlockFruitSheet()
        }
    }

    private fun showAd() {
        adManager.showAd(
            onReward = {
                viewModelScope.launch(Dispatchers.Default) {
                    val unlockedIndex = unlockRandomFruitUseCase()
                    _screenState.update {
                        it.copy(unlockedIndex = unlockedIndex)
                    }
                }
            },
            onAdFailedToShow = {
                viewModelScope.launch(Dispatchers.Default) {
                    _effect.emit(Effect.FailedToLoadAd)
                }
            }
        )
    }

    private fun toggleEnabled(index: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            toggleFruitEnabledUseCase(index)
        }
    }

    private fun hideUnlockFruitSheet() {
        _screenState.update {
            it.copy(unlockedIndex = null)
        }
    }
}