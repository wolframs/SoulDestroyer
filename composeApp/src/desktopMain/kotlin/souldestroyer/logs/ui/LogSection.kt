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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import souldestroyer.database.entity.LogEntry
import souldestroyer.simpleVerticalScrollbar

@Composable
fun LogSection(
    lazyListState: LazyListState = rememberLazyListState(),
    backgroundColor: Color,
    logList: List<LogEntry>,
    modifier: Modifier
) {
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
            logList.take(100)
        ) { logEntry ->
            LogEntryRow(
                dateTime = logEntry.dateTime,
                message = logEntry.message,
                type = logEntry.type
            )
        }
        item {
            Spacer(Modifier.height(4.dp))
        }
    }
}