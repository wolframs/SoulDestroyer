package souldestroyer.wallet

import souldestroyer.database.DatabaseModule
import souldestroyer.database.dao.WalletDAO
import souldestroyer.database.entity.WfWallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import souldestroyer.wallet.model.WalletListState
import java.util.concurrent.CopyOnWriteArrayList

class WalletRepository(
    val walletDAO: WalletDAO = DatabaseModule.getWalletDAO()
) : DisposableHandle {
    private val walletIOScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val walletListState = walletDAO.getAllInFlow().map { WalletListState(it) }
        .stateIn(
            scope = walletIOScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = WalletListState()
        )

    private val _wallets = CopyOnWriteArrayList<WfWallet>()
    val wallets: List<WfWallet> get() = _wallets

    companion object {
        @Volatile
        private var INSTANCE: WalletRepository? = null

        fun get(): WalletRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = WalletRepository()
                INSTANCE = instance
                instance
            }
        }
    }

    init {
        walletIOScope.launch {
            walletListState.collect {
                updateWalletsList(it.logList)
            }
        }
    }

    private fun updateWalletsList(newWallets: List<WfWallet>) {
        _wallets.clear()
        _wallets.addAll(newWallets)
    }

    fun addWallet(wfWallet: WfWallet) {
        walletIOScope.launch(Dispatchers.IO) {
            walletDAO.insert(wfWallet)
        }
    }

    override fun dispose() {
        walletIOScope.cancel("Wallet Repository disposed.")
    }

    suspend fun doesWalletExistInDB(publicKeyString: String): Boolean {
        return walletDAO.doesWalletExist(publicKeyString)
    }
}