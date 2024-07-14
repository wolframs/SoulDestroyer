package souldestroyer.database.converters

import androidx.room.TypeConverter

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