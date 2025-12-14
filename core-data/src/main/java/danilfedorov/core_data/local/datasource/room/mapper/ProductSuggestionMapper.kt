package danilfedorov.core_data.local.datasource.room.mapper

import danilfedorov.core_data.local.datasource.room.entity.ProductSuggestionEntity
import danilfedorov.domain_core.model.ProductSuggestion

class ProductSuggestionMapper() {

    fun toDomain(entity: ProductSuggestionEntity): ProductSuggestion =
        ProductSuggestion(
            displayName = entity.displayName,
            normalizedName = entity.normalizedName,
            usageCount = entity.usageCount
        )

    fun toEntity(model: ProductSuggestion): ProductSuggestionEntity =
        ProductSuggestionEntity(
            displayName = model.displayName,
            normalizedName = model.normalizedName,
            usageCount = model.usageCount
        )

    fun toDomainList(entities: List<ProductSuggestionEntity>): List<ProductSuggestion> =
        entities.map { toDomain(it) }

    fun toEntityList(models: List<ProductSuggestion>): List<ProductSuggestionEntity> =
        models.map { toEntity(it) }
}