package danilfedorov.domain_core.usecase.suggestion

import danilfedorov.domain_core.model.ProductSuggestion
import danilfedorov.domain_core.repository.ProductSuggestionRepository
import javax.inject.Inject

class InsertOrIncrementSuggestionUseCase @Inject constructor(
    private val suggestionRepository: ProductSuggestionRepository
) {
    suspend operator fun invoke(suggestions: List<String>) {
        if (suggestions.isEmpty()) return

        suggestionRepository.insertOrIncrement(suggestions)
    }
}