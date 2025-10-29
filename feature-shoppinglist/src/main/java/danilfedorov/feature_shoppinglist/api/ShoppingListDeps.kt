package danilfedorov.feature_shoppinglist.api

import android.content.res.Resources
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository


interface ShoppingListDeps {
    val shoppingListRepository: ShoppingListRepository
    val productRepository: ProductRepository
    val res: Resources

}