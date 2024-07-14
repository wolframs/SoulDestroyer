package souldestroyer.logs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.slf4j.LoggerFactory
import souldestroyer.database.DatabaseModule
import souldestroyer.database.dao.LogDAO
import souldestroyer.logs.model.LogEntry
import souldestroyer.logs.model.LogEntryType
import souldestroyer.logs.model.LogListModel

class LogRepository(
    private val logDAO: LogDAO = DatabaseModule.getLogDAO()
) : DisposableHandle {
    private val logIOScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val logger = LoggerFactory.getLogger("SoulDestroyer")

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
        .flowOn(Dispatchers.IO)
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

    fun logDebug(
        message: String,
        keys: List<String> = emptyList(),
        values: List<String> = emptyList()
    ) {
        logger.debug(message)
        log(message = message, type = LogEntryType.DEBUG, keys = keys, values = values)
    }

    fun logTransactInfo(
        message: String,
        keys: List<String> = emptyList(),
        values: List<String> = emptyList()
    ) {
        logger.info(getConsoleLogMsg("TRANSACT_INFO: $message", keys, values))
        log(message = message, type = LogEntryType.TRANSACT_INFO, keys = keys, values = values)
    }

    fun logTransactSuccess(
        message: String,
        keys: List<String> = emptyList(),
        values: List<String> = emptyList()
    ) {
        logger.info(getConsoleLogMsg("TRANSACT_SUCCESS: $message", keys, values))
        log(message = message, type = LogEntryType.TRANSACT_SUCCESS, keys = keys, values = values)
    }

    fun logInfo(
        message: String,
        keys: List<String> = emptyList(),
        values: List<String> = emptyList()
    ) {
        logger.info(getConsoleLogMsg(message, keys, values))
        log(message = message, type = LogEntryType.INFO, keys = keys, values = values)
    }

    fun logWarning(
        message: String,
        keys: List<String> = emptyList(),
        values: List<String> = emptyList()
    ) {
        logger.warn(getConsoleLogMsg(message, keys, values))
        log(message = message, type = LogEntryType.WARNING, keys = keys, values = values)
    }

    fun logError(
        message: String,
        keys: List<String> = emptyList(),
        values: List<String> = emptyList()
    ) {
        logger.error(getConsoleLogMsg(message, keys, values))
        log(message = message, type = LogEntryType.ERROR, keys = keys, values = values)
    }

    fun logSuccess(
        message: String,
        keys: List<String> = emptyList(),
        values: List<String> = emptyList()
    ) {
        logger.info(getConsoleLogMsg(message, keys, values))
        log(message = message, type = LogEntryType.SUCCESS, keys = keys, values = values)
    }

    fun log(
        message: String,
        type: LogEntryType,
        keys: List<String> = emptyList(),
        values: List<String> = emptyList()
    ) {
        logIOScope.launch {
            val keysCount = keys.count()
            val valuesCount = values.count()
            if (keysCount != valuesCount) {
                logKeyCountDoesNotMatchValueCount(message, keys, values)
                return@launch
            }
            logDAO.insert(
                if (keysCount + valuesCount >= 2)
                    LogEntry(message = message, type = type, keys = keys, values = values)
                else
                    LogEntry(message = message, type = type)
            )
        }
    }

    private suspend fun logKeyCountDoesNotMatchValueCount(
        message: String,
        keys: List<String>,
        values: List<String>
    ) {
        logDAO.insert(
            LogEntry(
                message = "Could not insert log entry '$message', because count of keys (${keys.count()}) did " +
                        "not match count of values (${values.count()}).",
                type = LogEntryType.WARNING
            )
        )
    }

    private fun getConsoleLogMsg(message: String, keys: List<String>, values: List<String>): String {
        val stringBuilder = StringBuilder()

        stringBuilder.appendLine(message)

        if (keys.isNotEmpty()) {
            stringBuilder.append("KEYS: ")
            stringBuilder.appendLine(keys.joinToString("|"))
            stringBuilder.append("VALUES: ")
            stringBuilder.appendLine(values.joinToString("|"))
        }

        return stringBuilder.toString()
    }
}