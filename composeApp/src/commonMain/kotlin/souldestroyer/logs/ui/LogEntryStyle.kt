package souldestroyer.logs.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.ktx.blend
import souldestroyer.logs.model.LogEntryType

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

        LogEntryType.SUCCESS,
        LogEntryType.TRANSACT_SUCCESS ->
            Color.Green.blend(MaterialTheme.colorScheme.secondaryContainer, 0.8f)

        LogEntryType.DEBUG ->
            MaterialTheme.colorScheme.secondaryContainer.blend(Color.Gray, 0.15f).copy(alpha = 0.8f)

        else ->
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.75f)
    }
}