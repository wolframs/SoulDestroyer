package souldestroyer.wallet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import souldestroyer.logs.LogRepository
import souldestroyer.wallet.domain.WalletImportSelectedMethod
import souldestroyer.wallet.domain.WalletManager

class Wallets(
    private val logRepo: LogRepository = LogRepository.instance(),
    private val walletRepository: WalletRepository = WalletRepository.instance(),
    val manager: WalletManager = WalletManager
) {
    val wList: MutableList<WalletImpl> = mutableListOf()

    companion object {
        @Volatile
        private var INSTANCE: Wallets? = null

        fun instance(): Wallets {
            return INSTANCE ?: synchronized(this) {
                val instance = Wallets()
                INSTANCE = instance
                instance
            }
        }
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val walletCount = walletRepository.walletDAO.getCount()
            if (walletCount <= 0) {
                logRepo.logInfo("No wallets in database so far.")
                return@launch
            }

            logRepo.logInfo("Loading $walletCount wallets from database...")

            val wallets = walletRepository.walletDAO.getAll()
            wallets.forEach { wfWallet ->
                val walletToAdd = WalletImpl.fromDatabaseWfWallet(wfWallet = wfWallet)
                if (wList.any { it.publicKey == walletToAdd.publicKey }) {
                    val indexOfWalletToReplace = wList.indexOfFirst { it.publicKey == walletToAdd.publicKey }
                    wList[indexOfWalletToReplace] = walletToAdd
                } else {
                    wList.add(walletToAdd)
                }
            }
        }
    }

    fun createNew(tag: String) {
        WalletManager.newWallet(tag)
    }

    fun createFromSecret(method: WalletImportSelectedMethod, tag: String, secretString: String) {
        WalletManager.walletFromSecret(method, tag, secretString)
    }

    fun createFromMnemonic(mnemonic: List<String>, passphrase: String) {
        WalletManager.walletFromMnemonic(mnemonic, passphrase)
    }
}
