package danilfedorov.core_data.remote.realtime.model

data class FirebaseProduct(
    val firebaseId: String = "",
    val name: String = "",
    val amount: String = "",
    val unit: String = "",
    val selected: Boolean = false
)