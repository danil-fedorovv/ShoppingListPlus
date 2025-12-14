package danilfedorov.domain_core.repository

import danilfedorov.domain_core.model.ProductSuggestion
import kotlinx.coroutines.flow.Flow

interface ProductSuggestionRepository {
    fun searchSuggestions(query: String): Flow<List<ProductSuggestion>>

    suspend fun insertOrIncrement(rawNames: List<String>)
}