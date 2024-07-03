package souldestroyer.database

import androidx.room.BuiltInTypeConverters
import souldestroyer.database.converters.LocalDateTimeConverter
import souldestroyer.database.converters.LogEntryConverter
import souldestroyer.database.converters.UUIDConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import souldestroyer.database.dao.LogDAO
import souldestroyer.database.dao.WalletDAO
import souldestroyer.logs.model.LogEntry
import souldestroyer.wallet.model.WfWallet

@Database(
    entities = [
        WfWallet::class,
        LogEntry::class
    ],
    version = 5,
    exportSchema = true
)
@TypeConverters(LogEntryConverter::class, LocalDateTimeConverter::class, UUIDConverter::class, builtInTypeConverters = BuiltInTypeConverters())
abstract class WfDatabase : RoomDatabase() {
    abstract fun walletDAO(): WalletDAO
    abstract fun logDAO(): LogDAO
}