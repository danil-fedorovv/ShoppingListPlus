package danilfedorov.domain_core.model

data class Product(
    val roomId: Long = 0,
    val firebaseId : String? = null,
    val listId: Long = 0,
    val name: String,
    val isSelected: Boolean = false,
    val amount: String = "",
    val unit: String = "",
)