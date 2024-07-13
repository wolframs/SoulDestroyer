package souldestroyer.logs.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.materialkolor.ktx.blend
import com.materialkolor.ktx.blendHue
import com.materialkolor.ktx.harmonize
import com.materialkolor.ktx.lighten
import souldestroyer.ColorFancies.modColor
import souldestroyer.logs.LogEntryType

object LogEntryStyle {
    @Composable
    fun getLogEntryRowColor(type: LogEntryType) = when (type) {
        LogEntryType.ERROR ->
            MaterialTheme.colorScheme.onError

        LogEntryType.WARNING ->
            MaterialTheme.colorScheme.onError.blend(Color.Yellow, 0.2f)
//            modColor(
//                MaterialTheme.colorScheme.onErrorContainer,
//                desaturateBy = 0.5f
//            )

        LogEntryType.SUCCESS ->
            Color.Green.blend(MaterialTheme.colorScheme.onSecondaryContainer, 0.8f)

        else ->
            MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.75f)
    }

    @Composable
    fun getLogEntryRowBackgroundColor(type: LogEntryType) = when (type) {
        LogEntryType.ERROR ->
            MaterialTheme.colorScheme.error

        LogEntryType.WARNING ->
            MaterialTheme.colorScheme.error.blend(Color.Yellow, 0.3f).copy(alpha = 0.8f)
//            modColor(
//                MaterialTheme.colorScheme.errorContainer,
//                desaturateBy = 0.5f
//            )

        LogEntryType.SUCCESS ->
            Color.Green.blend(MaterialTheme.colorScheme.secondaryContainer, 0.8f)
//            modColor(
//                color = MaterialTheme.colorScheme.secondaryContainer.blend(Color.Green, 0.285f),
//                brightenBy = -0.2f
//            )

        else ->
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.75f)
    }
}