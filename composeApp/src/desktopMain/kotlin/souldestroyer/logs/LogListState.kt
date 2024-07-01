package souldestroyer.logs

import souldestroyer.database.entity.LogEntry

data class LogListState(
    val logList: List<LogEntry> = listOf()
)
