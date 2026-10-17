package danilfedorov.feature_add_product.internal.presenter.mvi

import androidx.compose.runtime.Immutable
import danilfedorov.domain_core.model.ProductSuggestion

@Immutable
data class ScreenState(
    val searchInput: String = "",
    val suggestions: List<ProductSuggestion> = emptyList(),
    val productsToAdd: Set<String> = emptySet(),
    val initialProducts: Set<String> = emptySet()
)
