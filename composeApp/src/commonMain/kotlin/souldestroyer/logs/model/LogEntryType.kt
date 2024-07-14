package souldestroyer.logs.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ForwardToInbox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class LogEntryType(val iconImageVector: ImageVector) {
    INFO(Icons.Default.Info),
    WARNING(Icons.Default.Warning),
    ERROR(Icons.Default.Close),
    SUCCESS(Icons.Default.ThumbUp),
    DEBUG(Icons.Default.SettingsEthernet),
    TRANSACT_INFO(Icons.AutoMirrored.Default.ForwardToInbox),
    TRANSACT_SUCCESS(Icons.Default.MarkEmailRead);

    companion object {
        fun fromName(name: String): LogEntryType = entries.find { it.name == name } ?: INFO
    }
}