package danilfedorov.domain_core.usecase.suggestion

import danilfedorov.domain_core.model.ProductSuggestion
import danilfedorov.domain_core.repository.ProductSuggestionRepository
import kotlinx.coroutines.flow.Flow

class SearchProductSuggestionsUseCase (
    private val suggestionRepository: ProductSuggestionRepository
) {
    operator fun invoke(query: String): Flow<List<ProductSuggestion>> =
        suggestionRepository.searchSuggestions(query)
}