package souldestroyer.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class ListOfStringConverter {
        @TypeConverter
        fun fromListOfString(value: List<String>?): String? {
            return value?.joinToString("´")
        }

        @TypeConverter
        fun toListOfString(value: String?): List<String>? {
            return value?.let {
                value.split('´')
            }
        }
    }