package danilfedorov.domain_core.model

data class Product(
    var id: Long = 0,
    var listId: Long,
    var name: String,
    var isSelected: Boolean = false,
    var amount: String = "",
    var unit: String = "",
)

