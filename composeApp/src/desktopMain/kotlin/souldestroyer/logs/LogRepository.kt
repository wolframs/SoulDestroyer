package souldestroyer.logs

import souldestroyer.database.DatabaseModule
import souldestroyer.database.dao.LogDAO
import souldestroyer.logs.model.LogEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import souldestroyer.logs.model.LogListModel

class LogRepository(
    private val logDAO: LogDAO = DatabaseModule.getLogDAO()
): DisposableHandle {
    private val logIOScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        @Volatile
        private var INSTANCE: LogRepository? = null

        fun instance(): LogRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = LogRepository()
                instance.clearLogs()
                INSTANCE = instance
                instance
            }
        }
    }

    override fun dispose() {
        logIOScope.cancel("Log Repository disposed.")
    }

    fun logList() = logDAO
        .loadLatestLog()
        .distinctUntilChanged()
        .map { LogListModel(it) }
        .stateIn(
            scope = logIOScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LogListModel()
        )

    fun clearLogs() {
        runBlocking(Dispatchers.IO) {
            logDAO.deleteAllOlderThan(
                Clock.System.now().minus(10, DateTimeUnit.SECOND).toLocalDateTime(
                    TimeZone.currentSystemDefault()
                )
            )
        }
    }

    fun logInfo(message: String) {
        log(message = message, type = LogEntryType.INFO)
    }

    fun logWarning(message: String) {
        log(message = message, type = LogEntryType.WARNING)
    }

    fun logError(message: String) {
        log(message = message, type = LogEntryType.ERROR)
    }

    fun logSuccess(message: String) {
        log(message = message, type = LogEntryType.SUCCESS)
    }

    fun log(message: String, type: LogEntryType, localDateTime: LocalDateTime? = null) {
        logIOScope.launch {
            logDAO.insert(
                if (localDateTime != null)
                    LogEntry(dateTime = localDateTime, message = message, type = type)
                else
                    LogEntry(message = message, type = type)
            )
        }
    }
}