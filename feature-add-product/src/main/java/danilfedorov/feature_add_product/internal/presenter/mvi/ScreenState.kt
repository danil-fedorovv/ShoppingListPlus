package danilfedorov.feature_add_product.internal.presenter.mvi

import danilfedorov.domain_core.model.ProductSuggestion

data class ScreenState(
    val searchInput: String = "",
    val suggestions: List<ProductSuggestion> = emptyList(),
    val productsToAdd: Set<String> = emptySet(),
    val initialProducts: Set<String> = emptySet(),
    val voiceSuggestions: List<ProductSuggestion> = emptyList(),
    val isPremium: Boolean = false
)
