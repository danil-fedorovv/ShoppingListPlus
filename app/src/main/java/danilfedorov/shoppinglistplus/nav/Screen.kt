package danilfedorov.shoppinglistplus.nav

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data class ProductList(val shoppingListId: Long) : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Archive: Screen
}