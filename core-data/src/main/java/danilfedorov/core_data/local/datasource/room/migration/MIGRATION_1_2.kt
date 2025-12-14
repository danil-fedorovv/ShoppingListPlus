package danilfedorov.core_data.local.datasource.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE lists ADD COLUMN remoteListId TEXT")
        db.execSQL("ALTER TABLE items ADD COLUMN firebaseId TEXT")

        db.execSQL(
            """
            CREATE TABLE product_suggestions (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                displayName TEXT NOT NULL,
                normalizedName TEXT NOT NULL,
                usageCount INTEGER NOT NULL DEFAULT 0  
            )
            """.trimIndent()
        )
    }
}