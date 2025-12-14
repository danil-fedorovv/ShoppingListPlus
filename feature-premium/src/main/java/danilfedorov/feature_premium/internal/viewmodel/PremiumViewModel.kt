package danilfedorov.feature_premium.internal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.domain_core.usecase.premium.GetPremiumExpirationUseCase
import danilfedorov.domain_core.usecase.premium.PremiumStatusFlowUseCase
import danilfedorov.domain_core.usecase.premium.RefreshPremiumStateUseCase
import danilfedorov.feature_premium.internal.enum.PremiumType
import danilfedorov.feature_premium.internal.mvi.Action
import danilfedorov.feature_premium.internal.mvi.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rustore.sdk.pay.PurchaseInteractor
import ru.rustore.sdk.pay.model.ProductId
import ru.rustore.sdk.pay.model.ProductPurchaseParams

class PremiumViewModel(
    private val navHome: () -> Unit,
    private val purchaseInteractor: PurchaseInteractor,
    private val getPremiumExpirationUseCase: GetPremiumExpirationUseCase,
    private val premiumStatusFlowUseCase: PremiumStatusFlowUseCase,
    private val refreshPremiumStateUseCase: RefreshPremiumStateUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val result = withContext(Dispatchers.IO) {
                premiumStatusFlowUseCase().first()
            }
            _screenState.update {
                it.copy(isPremium = result)
            }
            if(_screenState.value.isPremium) {
                val date = getPremiumExpirationUseCase()
                _screenState.update {
                    it.copy(premiumExpirationDate = date)
                }
            }
        }
    }

    fun action(action: Action) {
        when (action) {
            is Action.NavHome -> navHome()
            is Action.Subscribe -> subscribe()
            is Action.SelectType -> selectType(action.type)
            is Action.HideCongratsSheet -> hideCongratsSheetVisible()
        }
    }

    private fun subscribe() {
        val params = ProductPurchaseParams(
            productId = ProductId(_screenState.value.subscriptionType.productId)
        )
        purchaseInteractor
            .purchase(params)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _screenState.update {
                        it.copy(isPremium = true, isCongratsSheetVisible = true)
                    }
                    val premiumExpirationDate = withContext(Dispatchers.IO) {
                        refreshPremiumStateUseCase()
                        getPremiumExpirationUseCase()
                    }

                    _screenState.update {
                        it.copy(premiumExpirationDate = premiumExpirationDate)
                    }
                }
            }
            .addOnFailureListener {
                Log.i("rustore1", "$it")
            }
    }

    private fun selectType(type: PremiumType) {
        _screenState.update { it.copy(subscriptionType = type) }
    }

    private fun hideCongratsSheetVisible() {
        _screenState.update { it.copy(isCongratsSheetVisible = false)}
    }

}