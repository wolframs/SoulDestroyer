package souldestroyer.wallet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.LogRepository

class Wallets(
    private val logRepo: LogRepository = LogRepository.instance(),
    private val walletRepository: WalletRepository = WalletRepository.get()
) {
    val wList: MutableList<WalletImpl> = mutableListOf()

    companion object {
        @Volatile
        private var INSTANCE: Wallets? = null

        fun get(): Wallets {
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
            wallets.forEach {
//                walletRepository.walletDAO.delete(it)
                wList.add(
                    WalletImpl.fromDatabaseWfWallet(wfWallet = it)
                )
            }

            logRepo.logInfo(
                message = "WalletImplList instantiated." +
                        if (wList.isNotEmpty()) {
                            "\n\n- " +
                                    wList.joinToString(separator = "\n\n- ") {
                                        it.toString()
                                    }
                        } else ""
            )
        }
    }

    fun getByPublicKeyString(publicKeyString: String): WalletImpl? {
        return wList.firstOrNull { it.publicKey.toString() == publicKeyString }
    }

    fun createWallet(tag: String) {
        val keypair = SolKeypair.generate()
        wList.add(
            WalletImpl(keypair, tag)
        )
    }

    fun walletFromPrivateKey(privateKey: ByteArray) {
        try {
            val keypair = SolKeypair.fromPrivateKey(privateKey)
            val wallet = WalletImpl(
                keypair = keypair,
                tag = "TEST"
            )
            wallet.sendMemoInitTransaction()
        } catch (e: Throwable) {
            LogRepository.instance().log(
                message = e.message ?: "walletFromMnemonic() failed.",
                type = LogEntryType.ERROR
            )
        }
    }

    fun walletFromMnemonic(mnemonic: List<String>, passphrase: String) {
        try {
            /*val test = HotAccount.fromMnemonic(
                words = mnemonic,
                passphrase = passphrase
            )*/
        } catch (e: Throwable) {
            LogRepository.instance().log(
                message = e.message ?: "walletFromMnemonic() failed.",
                type = LogEntryType.ERROR
            )
        }
    }
}
