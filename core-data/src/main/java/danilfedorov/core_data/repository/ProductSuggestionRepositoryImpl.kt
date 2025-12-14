package danilfedorov.core_data.repository

import danilfedorov.core_data.local.datasource.room.dao.ProductSuggestionDao
import danilfedorov.core_data.local.datasource.room.entity.ProductSuggestionEntity
import danilfedorov.core_data.local.datasource.room.mapper.ProductSuggestionMapper
import danilfedorov.domain_core.model.ProductSuggestion
import danilfedorov.domain_core.repository.ProductSuggestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductSuggestionRepositoryImpl(
    private val dao: ProductSuggestionDao,
    private val mapper: ProductSuggestionMapper
) : ProductSuggestionRepository {

    override fun searchSuggestions(query: String): Flow<List<ProductSuggestion>> {
        val normalizedQuery = query.trim().lowercase()
        return dao.searchSuggestions(normalizedQuery)
            .map { entities -> mapper.toDomainList(entities) }
    }

    override suspend fun insertOrIncrement(rawNames: List<String>) {
        if (rawNames.isEmpty()) return

        rawNames.forEach { displayName ->

            val normalized = displayName.trim().lowercase()

            val exists = dao.getByNormalizedName(normalized)

            if (exists == null) {
                dao.insertIgnore(
                    ProductSuggestionEntity(
                        displayName = displayName,
                        normalizedName = normalized,
                        usageCount = 0
                    )
                )
            }

            dao.incrementUsage(normalized)
        }
    }
}