package danilfedorov.core_data.local.datasource.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "lists")
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isSortedByAlphabet: Boolean = false,
    val deletedAt: Long? = null,
    val isArchived: Boolean = false,
    val remoteListId: String? = null
)