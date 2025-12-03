package danilfedorov.core_data.local.datasource.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE lists ADD COLUMN remoteListId TEXT")
        db.execSQL("ALTER TABLE items ADD COLUMN firebaseId TEXT")
    }
}