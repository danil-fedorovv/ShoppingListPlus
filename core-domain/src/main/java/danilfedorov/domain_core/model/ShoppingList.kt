package danilfedorov.domain_core.model

data class ShoppingList(
    var id: Long = 0L,
    var name: String,
    var isSortedByAlphabet: Boolean = false,
    val deletedAt: Long? = null,
    val isArchived: Boolean = false
)