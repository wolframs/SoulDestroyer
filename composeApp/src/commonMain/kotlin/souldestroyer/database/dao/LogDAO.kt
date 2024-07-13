package souldestroyer.database.dao

import souldestroyer.logs.model.LogEntry
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface LogDAO {
    @Query("SELECT * FROM logentry ORDER BY dateTime DESC LIMIT :limit")
    fun loadLatestLog(limit: Int = 150): Flow<List<LogEntry>>

    @Insert
    suspend fun insert(vararg logEntries: LogEntry)

    @Query("DELETE FROM logentry")
    suspend fun deleteAll()

    @Query("DELETE FROM logentry WHERE dateTime < :localDateTime")
    suspend fun deleteAllOlderThan(localDateTime: LocalDateTime)
}