package danilfedorov.core_data.local.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import danilfedorov.core_data.local.datasource.room.entity.ProductSuggestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductSuggestionDao {

    @Query("""
    SELECT *, INSTR(normalizedName, :query) AS matchPosition 
    FROM product_suggestions
    WHERE normalizedName LIKE '%' || :query || '%'
    ORDER BY
        (INSTR(normalizedName, :query) = 1) DESC,   
        INSTR(normalizedName, :query) ASC,         
        usageCount DESC,
        displayName ASC
    LIMIT 20
""")
    fun searchSuggestions(query: String): Flow<List<ProductSuggestionEntity>>


    @Query("SELECT * FROM product_suggestions WHERE normalizedName = :name LIMIT 1")
    suspend fun getByNormalizedName(name: String): ProductSuggestionEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(entity: ProductSuggestionEntity)

    @Query("UPDATE product_suggestions SET usageCount = usageCount + 1 WHERE normalizedName = :normalizedName")
    suspend fun incrementUsage(normalizedName: String)

}

