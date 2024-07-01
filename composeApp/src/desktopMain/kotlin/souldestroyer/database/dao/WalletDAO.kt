package souldestroyer.database.dao

import souldestroyer.database.entity.WfWallet
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDAO {
    @Query("SELECT EXISTS(SELECT * FROM wfwallet WHERE publicKey = :publicKeyString)")
    suspend fun doesWalletExist(publicKeyString: String) : Boolean

    @Query("SELECT * FROM wfwallet")
    fun getAllInFlow(): Flow<List<WfWallet>>

    @Query("SELECT * FROM wfwallet")
    suspend fun getAll(): List<WfWallet>

    @Query("SELECT COUNT(*) FROM wfwallet")
    suspend fun getCount(): Int

    @Query("SELECT * FROM wfwallet WHERE publicKey = :publicKeyString LIMIT 1")
    fun loadWallet(publicKeyString: String): Flow<WfWallet>

    @Query("UPDATE wfwallet SET balance = :balance WHERE publicKey = :publicKeyString")
    suspend fun updateBalance(publicKeyString: String, balance: Double)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg wfWallets: WfWallet)

    @Delete
    suspend fun delete(wfWallet: WfWallet)
}