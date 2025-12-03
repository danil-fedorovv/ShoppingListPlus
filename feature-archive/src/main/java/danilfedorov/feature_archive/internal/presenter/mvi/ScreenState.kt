package danilfedorov.feature_archive.internal.presenter.mvi

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList

data class ScreenState(
    val archiveLists: List<ShoppingList> = emptyList(),
    val progress: Map<Long, String> = emptyMap(),
    val isLoading: Boolean = true,
    val isArchiveProductsExpanded: ShoppingList? = null,
    val productList: List<Product> = emptyList()
)
