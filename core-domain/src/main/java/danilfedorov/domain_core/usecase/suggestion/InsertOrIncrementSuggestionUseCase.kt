package danilfedorov.domain_core.usecase.suggestion

import danilfedorov.domain_core.model.ProductSuggestion
import danilfedorov.domain_core.repository.ProductSuggestionRepository

class InsertOrIncrementSuggestionUseCase(
    private val suggestionRepository: ProductSuggestionRepository
) {
    suspend operator fun invoke(suggestions: List<String>) {
        if (suggestions.isEmpty()) return

        suggestionRepository.insertOrIncrement(suggestions)
    }
}