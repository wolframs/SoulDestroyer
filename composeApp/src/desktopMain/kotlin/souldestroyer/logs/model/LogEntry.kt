package souldestroyer.logs.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.getLocalDateTimeNow
import kotlinx.datetime.LocalDateTime
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


