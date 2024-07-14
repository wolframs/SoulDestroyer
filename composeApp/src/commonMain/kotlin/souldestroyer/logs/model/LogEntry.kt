package souldestroyer.logs.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import souldestroyer.logs.getLocalDateTimeNow
import java.util.UUID

@Entity
data class LogEntry(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    val dateTime: LocalDateTime = getLocalDateTimeNow(),
    val message: String,
    val type: LogEntryType,
    /** Delimiter: ´*/
    val keys: List<String>? = null,
    /** Delimiter: ´*/
    val values: List<String>? = null
)


