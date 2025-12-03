package danilfedorov.shoppinglistplus.nav

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Shoppinglist : Screen

    @Serializable
    data class ProductList(val shoppingListId: Long) : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Archive: Screen

    @Serializable
    data object Subscription: Screen

    @Serializable
    data object FruitAlbum: Screen

    @Serializable
    data object AddProduct: Screen
}