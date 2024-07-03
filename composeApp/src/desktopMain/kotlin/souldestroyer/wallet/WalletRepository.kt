package souldestroyer.wallet

import souldestroyer.database.DatabaseModule
import souldestroyer.database.dao.WalletDAO
import souldestroyer.wallet.model.WfWallet
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
import souldestroyer.wallet.model.WalletListModel
import java.util.concurrent.CopyOnWriteArrayList

class WalletRepository(
    val walletDAO: WalletDAO = DatabaseModule.getWalletDAO()
) : DisposableHandle {
    private val walletIOScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val walletListModel = walletDAO.getAllInFlow()
        .distinctUntilChanged()
        .map { WalletListModel(it) }
        .stateIn(
            scope = walletIOScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = WalletListModel()
        )

    private val _wallets = CopyOnWriteArrayList<WfWallet>()

    companion object {
        @Volatile
        private var INSTANCE: WalletRepository? = null

        fun instance(): WalletRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = WalletRepository()
                INSTANCE = instance
                instance
            }
        }
    }

    init {
        walletIOScope.launch {
            walletListModel.collect {
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