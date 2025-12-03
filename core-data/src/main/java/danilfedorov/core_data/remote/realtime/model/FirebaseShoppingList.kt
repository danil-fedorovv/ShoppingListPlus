package danilfedorov.core_data.remote.realtime.model

data class FirebaseShoppingList(
    val name: String = "",
    val createdAt: Any? = null,
    val items: Map<String, FirebaseProduct>? = null
)