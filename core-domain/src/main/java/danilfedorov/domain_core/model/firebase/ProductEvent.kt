package danilfedorov.domain_core.model.firebase

import danilfedorov.domain_core.model.Product

sealed class ProductEvent {
    data class Added(val product: Product) : ProductEvent()
    data class Changed(val product: Product) : ProductEvent()
    data class Removed(val product: Product) : ProductEvent()
}