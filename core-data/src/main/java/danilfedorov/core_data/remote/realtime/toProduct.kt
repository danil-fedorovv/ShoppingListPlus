package danilfedorov.core_data.remote.realtime

import com.google.firebase.database.DataSnapshot
import danilfedorov.domain_core.model.Product

fun DataSnapshot.toProduct(): Product? {
    val firebaseId = this.key ?: return null

    val amount = this.child("amount").getValue(String::class.java).orEmpty()
    val name = this.child("name").getValue(String::class.java) ?: return null
    val unit = this.child("unit").getValue(String::class.java).orEmpty()
    val selected = this.child("selected").getValue(Boolean::class.java) ?: false

    return Product(
        firebaseId = firebaseId,
        name = name,
        amount = amount,
        unit = unit,
        isSelected = selected
    )
}