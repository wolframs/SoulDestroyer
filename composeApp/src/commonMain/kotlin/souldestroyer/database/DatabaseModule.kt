package souldestroyer.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import souldestroyer.database.dao.LogDAO
import souldestroyer.database.dao.WalletDAO
import kotlinx.coroutines.Dispatchers
import java.io.File

object DatabaseModule {
    @Volatile
    private var INSTANCE: WfDatabase? = null

    private fun getDatabase(): WfDatabase {
        return INSTANCE ?: synchronized(this) {

            val dbFile = File(System.getProperty("java.io.tmpdir"), "wolf_sol_souldestroyer.db")
            val instance = Room.databaseBuilder<WfDatabase>(
                name = dbFile.absolutePath
            )
                .fallbackToDestructiveMigration(false)
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
            INSTANCE = instance
            instance
        }
    }

    fun getLogDAO(): LogDAO = getDatabase().logDAO()

    fun getWalletDAO(): WalletDAO = getDatabase().walletDAO()
}