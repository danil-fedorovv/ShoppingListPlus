package danilfedorov.feature_settings.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.common.ad.AdManager
import danilfedorov.domain_core.usecase.appearance.ChangeColorIndexUseCase
import danilfedorov.domain_core.usecase.appearance.ChangeThemeUseCase
import danilfedorov.domain_core.usecase.appearance.ColorIndexFlowUseCase
import danilfedorov.domain_core.usecase.appearance.ThemeFlowUseCase
import danilfedorov.domain_core.usecase.premium.PremiumStatusFlowUseCase
import danilfedorov.feature_settings.internal.presenter.mvi.Action
import danilfedorov.feature_settings.internal.presenter.mvi.Effect
import danilfedorov.feature_settings.internal.presenter.mvi.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val navBack: () -> Unit,
    private val navFruitCollection: () -> Unit,
    private val navPremium: () -> Unit,
    themeFlowUseCase: ThemeFlowUseCase,
    colorIndexFlowUseCase: ColorIndexFlowUseCase,
    private val changeColorIndexUseCase: ChangeColorIndexUseCase,
    private val changeThemeUseCase: ChangeThemeUseCase,
    private val adManager: AdManager,
    private val premiumStatusFlowUseCase: PremiumStatusFlowUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            themeFlowUseCase().collect { isDark ->
                _screenState.update { it.copy(darkTheme = isDark ?: false) }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            colorIndexFlowUseCase().collect { index ->
                _screenState.update { it.copy(colorIndex = index ?: 0) }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            premiumStatusFlowUseCase().collect { isPremium ->
                _screenState.update {
                    it.copy(isPremium = isPremium)
                }
            }
        }
    }

    fun action(action: Action) {
        when (action) {
            is Action.NavBack -> navBack()
            Action.NavFruitCollection -> navFruitCollection()
            Action.NavPremium -> navPremium()

            is Action.SetDarkTheme -> setDarkTheme(action.darkTheme)
            is Action.SaveColor -> changeColor(action.colorIndex)

            Action.RateUs -> onRateUsClick()
            Action.ContactUs -> onContactUsClick()
            Action.HideChangeColorSheet -> {
                _screenState.update {
                    it.copy(isChangeColorSheetVisible = false)
                }
            }

            Action.ShowChangeColorSheet -> {
                _screenState.update {
                    it.copy(isChangeColorSheetVisible = true)
                }
            }
        }
    }

    private fun setDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            changeThemeUseCase(darkTheme)
        }
    }

    private fun changeColor(colorIndex: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            if (_screenState.value.isPremium) {
                changeColorIndexUseCase(colorIndex)
            } else {
                showAd(colorIndex)
            }
        }
    }

    private fun showAd(colorIndex: Int) {
        adManager.showAd(
            onReward = {
                viewModelScope.launch(Dispatchers.IO) {
                    changeColorIndexUseCase(colorIndex)
                }
            },
            onAdFailedToShow = {
                viewModelScope.launch(Dispatchers.Default) {
                    _effect.emit(Effect.FailedToLoadAd)
                }
            }
        )
    }

    private fun onRateUsClick() {
        viewModelScope.launch(Dispatchers.Default) {
            _effect.emit(Effect.NavRateUs)
        }
    }

    private fun onContactUsClick() {
        viewModelScope.launch(Dispatchers.Default) {
            _effect.emit(Effect.NavContactUs)
        }
    }

    override fun onCleared() {
        super.onCleared()
        adManager.clear()
    }
}