package souldestroyer.wallet.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import souldestroyer.logs.LogRepository
import souldestroyer.logs.model.LogEntryType
import souldestroyer.wallet.SolKeypair
import souldestroyer.wallet.WalletImpl
import souldestroyer.wallet.WalletRepository
import souldestroyer.wallet.Wallets
import souldestroyer.wallet.model.WfWallet

object WalletManager {
    val walletScope = CoroutineScope(Dispatchers.IO) + SupervisorJob()
    val mutex = Mutex()

    private val walletRepo = WalletRepository.instance()
    private val logRepo = LogRepository.instance()

    fun getByPublicKey(publicKeyString: String): WalletImpl? {
        return Wallets.instance().wList.firstOrNull { it.publicKey.toString() == publicKeyString }
    }

    fun getByTag(tag: String): WalletImpl? {
        return Wallets.instance().wList.firstOrNull { it.tag == tag }
    }

    fun walletFromMnemonic(mnemonic: List<String>, passphrase: String) {
        try {
            /*val test = HotAccount.fromMnemonic(
                words = mnemonic,
                passphrase = passphrase
            )*/
//            Wallets.instance().wList.add(
//                WalletImpl(keypair, tag)
//            )
            TODO()
        } catch (e: Throwable) {
            LogRepository.instance().log(
                message = "walletFromMnemonic(): " + (e.message ?: "Operation failed."),
                type = LogEntryType.ERROR
            )
        }
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

                else -> throw IllegalArgumentException("$method can't be handled by walletFromSecret()")
            }

            Wallets.instance().wList.add(
                WalletImpl(keypair, tag)
            )
        } catch (e: Throwable) {
            LogRepository.instance().log(
                message = "walletFromSecret(): " + (e.message ?: "Operation failed."),
                type = LogEntryType.ERROR
            )
        }
    }

    fun newWallet(tag: String): Boolean {
        val tagPurposeExplainer = "Tags serve the purpose of helping you distinguish between wallets in this app."

        if (tag.length < 3) {
            LogRepository.instance().logWarning(
                message = "Please provide a tag with a length of at least 3 characters.\n$tagPurposeExplainer"
            )
            return false
        }

        if (getByTag(tag) != null) {
            LogRepository.instance().logWarning(
                message = "A wallet with the tag $tag already exists.\n$tagPurposeExplainer"
            )
            return false
        }

        try {
            val keypair = SolKeypair.generate()
            Wallets.instance().wList.add(
                WalletImpl(keypair, tag)
            )
            return true
        } catch (e: Throwable) {
            logRepo.logError("Wallet creation failed:\n\n${e.message ?: "Unknown error."}")
            return false
        }
    }

    fun insertToDatabaseIfNotExists(keypair: SolKeypair, tag: String, onCompletion: () -> Unit) {
        val wfWallet = WfWallet(
            publicKey = keypair.publicKey.toString(),
            privateKey = keypair.secretKey,
            tag = tag
        )
        walletScope.launch(Dispatchers.IO) {
            try {
                if (!walletRepo.doesWalletExistInDB(wfWallet.publicKey)) {
                    walletRepo.addWallet(wfWallet)
                    logRepo.logSuccess("Added wallet \"${wfWallet.tag}\" (${wfWallet.publicKey}) to database.")
                }
            } catch (e: Throwable) {
                logRepo.logError(
                    message = "Could not insert \"${wfWallet.tag}\" (${wfWallet.publicKey}) to database:\n\n" +
                            (e.message ?: "Unknown error.")
                )
            }
        }.invokeOnCompletion {
            onCompletion()
        }
    }

    fun ensureAtLeastOneAccountIsActive() {
        walletScope.launch {
            mutex.withLock {
                val allWalletsInDatabase = walletRepo.walletDAO.getAll()
                if (allWalletsInDatabase.isEmpty()) {
                    return@launch
                }
                if (allWalletsInDatabase.none { it.isActiveAccount }) {
                    allWalletsInDatabase.first().let { firstWallet ->
                        walletRepo.setIsActiveAccount(firstWallet.publicKey, true)
                    }
                }
            }
        }
    }
}