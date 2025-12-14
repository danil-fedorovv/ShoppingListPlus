package danilfedorov.core_data.local.datasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_suggestions"
)
data class ProductSuggestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val displayName: String,
    val normalizedName: String,
    @ColumnInfo(defaultValue = "0")
    val usageCount: Long = 0
)