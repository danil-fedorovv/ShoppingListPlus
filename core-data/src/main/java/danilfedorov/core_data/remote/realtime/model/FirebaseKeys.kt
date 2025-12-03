package danilfedorov.core_data.remote.realtime.model

object FirebaseKeys {
    const val SHOPPING_LISTS = "shopping_lists"

    object ShoppingList {
        const val NAME = "name"
        const val ITEMS = "items"
    }

    object Product {
        const val NAME = "name"
        const val AMOUNT = "amount"
        const val UNIT = "unit"
        const val SELECTED = "selected"
    }
}