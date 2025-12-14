@file:OptIn(ExperimentalCoroutinesApi::class)

package danilfedorov.feature_add_product.internal.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ProductSuggestion
import danilfedorov.domain_core.usecase.premium.PremiumStatusFlowUseCase
import danilfedorov.domain_core.usecase.product.DeleteProductsUseCase
import danilfedorov.domain_core.usecase.product.ObserveProductListByListIdUseCase
import danilfedorov.domain_core.usecase.suggestion.InsertProductsFromNamesUseCase
import danilfedorov.domain_core.usecase.suggestion.InsertOrIncrementSuggestionUseCase
import danilfedorov.domain_core.usecase.suggestion.SearchProductSuggestionsUseCase
import danilfedorov.feature_add_product.internal.presenter.mvi.Action
import danilfedorov.feature_add_product.internal.presenter.mvi.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AddProductViewModel(
    private val shoppingListId: Long,
    private val remoteListId: String?,
    private val navBack: (Long, Int?) -> Unit,
    private val searchProductSuggestionsUseCase: SearchProductSuggestionsUseCase,
    private val insertProductsFromNamesUseCase: InsertProductsFromNamesUseCase,
    private val insertOrIncrementSuggestionUseCase: InsertOrIncrementSuggestionUseCase,
    private val observeProductListByListIdUseCase: ObserveProductListByListIdUseCase,
    private val deleteProductsUseCase: DeleteProductsUseCase,
    private val premiumStatusFlowUseCase: PremiumStatusFlowUseCase,
    ) : ViewModel() {
    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()
    private var initialProducts: List<Product> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            initialProducts = observeProductListByListIdUseCase(shoppingListId).first()
            val initialNames = initialProducts.map { it.name }.toSet()
            _screenState.update {
                it.copy(productsToAdd = initialNames, initialProducts = initialNames)
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            screenState
                .map { it.searchInput }
                .flatMapLatest { query ->
                    searchProductSuggestionsUseCase(query)
                }
                .collect { dbSuggestions ->
                    val currentInput = screenState.value.searchInput.trim()
                    val suggestions =
                        if (dbSuggestions.none { it.normalizedName == currentInput } && currentInput.isNotBlank()) {
                            val tempSuggestion = ProductSuggestion(
                                displayName = currentInput,
                                normalizedName = currentInput.trim().lowercase(),
                                usageCount = 1
                            )
                            listOf(tempSuggestion) + dbSuggestions
                        } else {
                            dbSuggestions
                        }
                    _screenState.update {
                        it.copy(suggestions = suggestions)
                    }
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
            is Action.OnChangeSearchInput -> onChangeSearchInput(action.input)
            Action.SaveProducts -> saveProducts()
            is Action.VoiceInputReceived -> saveProductsFromVoice(action.voiceInput)
            is Action.ToggleSuggestion -> toggleSuggestion(action.displayName)
        }
    }

    private fun onChangeSearchInput(input: String) {
        _screenState.update {
            it.copy(
                searchInput = input,
                voiceSuggestions = emptyList()
            )
        }
    }

    private fun toggleSuggestion(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return
        val searchInput = _screenState.value.searchInput.trim()

        _screenState.update { state ->
            if (state.productsToAdd.contains(trimmed))
                state.copy(productsToAdd = state.productsToAdd - trimmed, searchInput = "")
            else state.copy(
                productsToAdd = state.productsToAdd + trimmed, searchInput = ""
            )
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (searchInput.equals(trimmed, ignoreCase = true)) {
                insertOrIncrementSuggestionUseCase(
                    listOf(searchInput)
                )
            }
        }
    }

    fun saveProducts() {
        val productsToAdd = _screenState.value.productsToAdd

        val namesToAdd = productsToAdd - initialProducts.map { it.name }.toSet()

        navBack(shoppingListId, namesToAdd.size)


        CoroutineScope(Dispatchers.IO).launch {
            val productsToDelete = initialProducts.filter { it.name !in productsToAdd }
            if (namesToAdd.isNotEmpty()) {
                insertProductsFromNamesUseCase(
                    shoppingListId, remoteListId, namesToAdd.toList()
                )
            }

            if (productsToDelete.isNotEmpty()) {
                deleteProductsUseCase(
                    remoteListId, productsToDelete
                )
            }

            insertOrIncrementSuggestionUseCase(namesToAdd.toList())
        }
    }

    private fun saveProductsFromVoice(voiceInput: String) {
        val cleaned = voiceInput
            .replace(Regex("\\s+"), " ")
            .trim()

        if (cleaned.isBlank()) return

        val andSep = productsAndSeparator().trim()

        val unified = cleaned
            .replace(Regex("\\s+$andSep\\s+", RegexOption.IGNORE_CASE), ",")
            .replace(Regex("\\s*,\\s*"), ",")

        val rawNames = unified
            .split(',')
            .map { it.trim() }
            .filter { it.isNotBlank() && it.lowercase() != andSep }
            .distinct()

        if (rawNames.isEmpty()) return

        viewModelScope.launch(Dispatchers.Default) {

            val parsed = rawNames.map { name ->
                ProductSuggestion(
                    displayName = name,
                    normalizedName = name.lowercase()
                )
            }

            _screenState.update { state ->
                state.copy(
                    productsToAdd = state.productsToAdd + rawNames.toSet(),
                    voiceSuggestions = parsed
                )
            }
        }
    }

    private fun productsAndSeparator(): String {
        return if (Locale.getDefault().language == "ru") {
            " и "
        } else {
            " and "
        }
    }
}