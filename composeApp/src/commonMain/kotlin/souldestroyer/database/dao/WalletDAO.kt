package souldestroyer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import souldestroyer.wallet.model.WfWallet

@Dao
interface WalletDAO {
    @Query("SELECT * FROM wfwallet")
    suspend fun getAll(): List<WfWallet>

    @Query("SELECT * FROM wfwallet WHERE publicKey = :publicKeyString LIMIT 1")
    suspend fun get(publicKeyString: String): WfWallet

    @Query("SELECT COUNT(*) FROM wfwallet")
    suspend fun getCount(): Int

    @Query("SELECT EXISTS(SELECT * FROM wfwallet WHERE publicKey = :publicKeyString)")
    suspend fun doesWalletExist(publicKeyString: String) : Boolean

    @Query("SELECT * FROM wfwallet WHERE publicKey = :publicKeyString LIMIT 1")
    fun loadWallet(publicKeyString: String): Flow<WfWallet>

    @Query("SELECT * FROM wfwallet")
    fun getAllInFlow(): Flow<List<WfWallet>>

    @Query("UPDATE wfwallet SET balance = :balance WHERE publicKey = :publicKeyString")
    suspend fun updateBalance(publicKeyString: String, balance: Double)

    @Query("UPDATE wfwallet SET isActiveAccount = :newIsActiveAccountValue WHERE publicKey = :pubicKeyString")
    suspend fun setIsActiveAccount(pubicKeyString: String, newIsActiveAccountValue: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg wfWallets: WfWallet)

    @Query("DELETE FROM wfwallet WHERE publicKey = :publicKeyString")
    suspend fun delete(publicKeyString: String)

    @Delete
    suspend fun delete(wfWallet: WfWallet)
}