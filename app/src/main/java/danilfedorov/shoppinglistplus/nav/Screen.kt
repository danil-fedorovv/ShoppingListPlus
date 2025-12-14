package danilfedorov.shoppinglistplus.nav

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object ShoppingList : Screen

    @Serializable
    data class ProductList(val shoppingListId: Long, val addedCount: Int? = null) : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Archive: Screen

    @Serializable
    data object Subscription: Screen

    @Serializable
    data object FruitAlbum: Screen

    @Serializable
    data class AddProduct(val shoppingListId: Long, val remoteListId: String?): Screen
}