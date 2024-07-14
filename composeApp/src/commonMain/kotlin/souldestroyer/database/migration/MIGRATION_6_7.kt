package souldestroyer.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(connection: SQLiteConnection) {
        // Add the new 'WfWallet' column 'isActiveAccount' with a default value of false
        connection.execSQL("ALTER TABLE WfWallet ADD COLUMN isActiveAccount INTEGER NOT NULL DEFAULT 0")
    }
}