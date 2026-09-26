package danilfedorov.feature_productlist.api

import android.content.res.Resources
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository


interface ProductListDeps {
    val productRepository: ProductRepository
    val shoppingListRepository: ShoppingListRepository
    val res: Resources
}