package souldestroyer.logs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import souldestroyer.logs.model.LogEntry
import souldestroyer.logs.model.LogEntryType
import souldestroyer.settings.SettingsManager
import souldestroyer.shared.ui.simpleVerticalScrollbar

@Composable
fun LogSection(
    lazyListState: LazyListState = rememberLazyListState(),
    settingsManager: SettingsManager = SettingsManager(),
    backgroundColor: Color,
    logList: List<LogEntry>,
    modifier: Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val showVerboseLogs by settingsManager.showVerboseLogsFlow.distinctUntilChanged().collectAsState(initial = false)

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .simpleVerticalScrollbar(
                state = lazyListState
            )
    ) {
        item {
            Spacer(Modifier.height(4.dp))
        }
        items(
            items = logList.take(100).filter { logEntry ->
                showVerboseLogs || logEntry.type != LogEntryType.DEBUG
            },
            key = { logList.listIterator() }
        ) { logEntry ->
            LogEntryRow(
                dateTime = logEntry.dateTime,
                message = logEntry.message,
                type = logEntry.type,
                keys = logEntry.keys,
                clipboardManager = clipboardManager,
                values = logEntry.values
            )
        }
        /*items(
            logList.take(100).filter { logEntry ->
                showVerboseLogs || logEntry.type != LogEntryType.DEBUG
            }
        ) { logEntry ->
            LogEntryRow(
                dateTime = logEntry.dateTime,
                message = logEntry.message,
                type = logEntry.type,
                keys = logEntry.keys,
                clipboardManager = clipboardManager,
                values = logEntry.values
            )
        }*/
        item {
            Spacer(Modifier.height(4.dp))
        }
    }
}