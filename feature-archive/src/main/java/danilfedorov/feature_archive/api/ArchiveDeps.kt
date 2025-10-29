package danilfedorov.feature_archive.api

import android.content.res.Resources
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository

interface ArchiveDeps {
    val shoppingListRepository: ShoppingListRepository
    val productRepository: ProductRepository
    val res: Resources
}