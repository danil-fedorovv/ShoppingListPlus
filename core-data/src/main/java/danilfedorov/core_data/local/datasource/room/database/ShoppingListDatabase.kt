package danilfedorov.core_data.local.datasource.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import danilfedorov.core_data.R
import danilfedorov.core_data.local.datasource.room.dao.ProductDao
import danilfedorov.core_data.local.datasource.room.dao.ShoppingListDao
import danilfedorov.core_data.local.datasource.room.entity.ProductEntity
import danilfedorov.core_data.local.datasource.room.entity.ShoppingListEntity
import danilfedorov.core_data.local.datasource.room.migration.MIGRATION_1_2

@Database(
    entities = [ShoppingListEntity::class, ProductEntity::class],
    version = 2,
    exportSchema = true
)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun getShoppingItemDao(): ProductDao
    abstract fun getShoppingListDao(): ShoppingListDao

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
                        val features = listOf(
                            context.getString(R.string.feature_swipe) to false,
                            context.getString(R.string.feature_edit) to false,
                            context.getString(R.string.feature_multi_select) to false,
                            context.getString(R.string.feature_ps) to true
                        )
                        db.execSQL(
                            "INSERT INTO lists (id, name, isSortedByAlphabet, isArchived) VALUES (1, '${
                                context.getString(R.string.whats_new)
                            }', 0, 0)"
                        )
                        features.forEachIndexed { index, feature ->
                            val name = feature.first
                            val isSelected = if (feature.second) 1 else 0
                            val itemId = index + 1
                            db.execSQL(
                                "INSERT INTO items (id, listId, name, isSelected, amount, unit) " +
                                        "VALUES ($itemId, 1, '$name', $isSelected, '', '')"
                            )
                        }

                    }
                })
                .build()
        }
    }
}