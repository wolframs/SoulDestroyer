package souldestroyer.wallet.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.LogRepository
import souldestroyer.wallet.SolKeypair
import souldestroyer.wallet.WalletImpl
import souldestroyer.wallet.WalletRepository
import souldestroyer.wallet.Wallets
import souldestroyer.wallet.model.WfWallet

object WalletManager {
    val walletScope = CoroutineScope(Dispatchers.IO) + SupervisorJob()

    private val walletRepo = WalletRepository.instance()
    private val logRepo = LogRepository.instance()

    fun getByPublicKey(publicKeyString: String): WalletImpl? {
        return Wallets.instance().wList.firstOrNull { it.publicKey.toString() == publicKeyString }
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

    fun newWallet(tag: String) {
        val keypair = SolKeypair.generate()
        Wallets.instance().wList.add(
            WalletImpl(keypair, tag)
        )
    }

    fun insertToDatabaseIfNotExists(keypair: SolKeypair, tag: String, onCompletion: () -> Unit) {
        val wfWallet = WfWallet(
            publicKey = keypair.publicKey.toString(),
            privateKey = keypair.secretKey,
            tag = tag
        )
        walletScope.launch(Dispatchers.IO) {
            if (!walletRepo.doesWalletExistInDB(wfWallet.publicKey)) {
                walletRepo.addWallet(wfWallet)
                logRepo.logSuccess("Added $wfWallet to database.")
            }
        }.invokeOnCompletion {
            onCompletion()
        }
    }
}