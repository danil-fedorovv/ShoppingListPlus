package danilfedorov.feature_add_product.api

import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ProductSuggestionRepository

interface AddProductDeps {
    val productRepository: ProductRepository
    val productSuggestionRepository: ProductSuggestionRepository
}