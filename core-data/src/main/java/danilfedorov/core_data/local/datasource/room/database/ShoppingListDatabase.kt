package danilfedorov.core_data.local.datasource.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import danilfedorov.core_data.R
import danilfedorov.core_data.local.datasource.room.dao.ProductDao
import danilfedorov.core_data.local.datasource.room.dao.ProductSuggestionDao
import danilfedorov.core_data.local.datasource.room.dao.ShoppingListDao
import danilfedorov.core_data.local.datasource.room.entity.ProductEntity
import danilfedorov.core_data.local.datasource.room.entity.ProductSuggestionEntity
import danilfedorov.core_data.local.datasource.room.entity.ShoppingListEntity
import danilfedorov.core_data.local.datasource.room.migration.MIGRATION_1_2

@Database(
    entities = [ShoppingListEntity::class, ProductEntity::class, ProductSuggestionEntity::class],
    version = 2,
    exportSchema = true
)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun getShoppingItemDao(): ProductDao
    abstract fun getShoppingListDao(): ShoppingListDao
    abstract fun getProductSuggestionDao(): ProductSuggestionDao

    companion object {
        private var instance: ShoppingListDatabase? = null

        fun getDataBase(context: Context): ShoppingListDatabase {
            return instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): ShoppingListDatabase {
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = ShoppingListDatabase::class.java,
                name = "shoppinglist.db"
            )
                .addMigrations(MIGRATION_1_2)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        createWhatsNewList(db, context)
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        try {
                            // Проверяем, есть ли уже записи в таблице
                            val cursor = db.query("SELECT COUNT(*) FROM product_suggestions")
                            cursor.moveToFirst()
                            val count = cursor.getInt(0)
                            cursor.close()

                            // Если таблица пустая - загружаем
                            if (count == 0) {
                                preloadProductSuggestions(db, context)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            // В случае ошибки тоже попробуем загрузить
                            preloadProductSuggestions(db, context)
                        }                    }
                })
                .build()
        }

        private fun createWhatsNewList(db: SupportSQLiteDatabase, context: Context) {
            val features = listOf(
                context.getString(R.string.feature_swipe) to false,
                context.getString(R.string.feature_edit) to false,
                context.getString(R.string.feature_multi_select) to false,
                context.getString(R.string.feature_share) to false,
                context.getString(R.string.feature_add_product_screen) to false,
                context.getString(R.string.feature_voice_input) to false,
                context.getString(R.string.feature_ps) to true
            )

            db.execSQL(
                "INSERT INTO lists (id, name, isSortedByAlphabet, isArchived) VALUES (1, '${
                    context.getString(
                        R.string.whats_new
                    )
                }', 0, 0)"
            )
            features.forEachIndexed { index, (name, isSelected) ->
                val itemId = index + 1
                db.execSQL("INSERT INTO items (id, listId, name, isSelected, amount, unit) VALUES ($itemId, 1, '$name', ${if (isSelected) 1 else 0}, '', '')")
            }
        }

        private fun preloadProductSuggestions(db: SupportSQLiteDatabase, context: Context) {
            try {
                val locale = context.resources.configuration.locales[0]
                val language = locale.language

                val assetFile = when (language) {
                    "ru" -> "preloaded_suggestions_ru.json"
                    else -> "preloaded_suggestions_en.json"
                }

                val json = context.assets.open(assetFile)
                    .bufferedReader()
                    .use { it.readText() }

                val gson = Gson()
                val rawList: List<String> =
                    gson.fromJson(json, object : TypeToken<List<String>>() {}.type)

                rawList.forEach { raw ->
                    db.execSQL(
                        """
                        INSERT OR IGNORE INTO product_suggestions 
                        (displayName, normalizedName, usageCount) 
                        VALUES (?, ?, 0)
                        """.trimIndent(),
                        arrayOf(raw, raw)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

