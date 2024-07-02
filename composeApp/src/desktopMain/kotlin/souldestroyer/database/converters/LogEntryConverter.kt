package souldestroyer.database.converters

import androidx.room.TypeConverter
import souldestroyer.logs.LogEntryType

class LogEntryConverter {
    @TypeConverter
    fun toImageVector(value: String) = LogEntryType.fromName(value)

    @TypeConverter
    fun fromImageVector(value: LogEntryType) = value.name
}