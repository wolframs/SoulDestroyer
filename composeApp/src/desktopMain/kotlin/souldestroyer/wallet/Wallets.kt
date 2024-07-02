package souldestroyer.wallet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.LogRepository
import souldestroyer.wallet.ui.WalletImportSelectedMethod

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
            wallets.forEach { wfWallet ->
                val walletToAdd = WalletImpl.fromDatabaseWfWallet(wfWallet = wfWallet)
                if (wList.any { it.publicKey == walletToAdd.publicKey }) {
                    val indexOfWalletToReplace = wList.indexOfFirst { it.publicKey == walletToAdd.publicKey }
                    wList[indexOfWalletToReplace] = walletToAdd
                } else {
                    wList.add(walletToAdd)
                }
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

    fun createWallet(tag: String) {
        val keypair = SolKeypair.generate()
        wList.add(
            WalletImpl(keypair, tag)
        )
    }

    fun walletFromSecret(method: WalletImportSelectedMethod, tag: String, secretString: String) {
        try {
            val keypair: SolKeypair = when (method) {
                WalletImportSelectedMethod.PRIVATE_KEY -> {
                    SolKeypair.fromPrivateKey(secretString)
                }

                WalletImportSelectedMethod.BYTE_ARRAY -> {
                    val byteArray = secretString
                        .trim()
                        .removeSurrounding("[", "]")
                        .split(",")
                        .map { it.toInt().toByte() }
                        .take(32)
                        .toByteArray()
                    SolKeypair.fromByteArray(byteArray)
                }

                else -> throw IllegalArgumentException("Mnemonics can't be handled by walletFromPrivateKey()")
            }

            val wallet = WalletImpl(
                keypair = keypair,
                tag = tag
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
