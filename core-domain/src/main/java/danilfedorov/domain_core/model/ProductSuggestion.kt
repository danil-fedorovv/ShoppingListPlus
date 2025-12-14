package danilfedorov.domain_core.model

data class ProductSuggestion(
    val displayName: String,
    val normalizedName: String,
    val usageCount: Long = 0
)