package souldestroyer.wallet

import souldestroyer.Constants
import souldestroyer.sol.WfSolana
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import foundation.metaplex.rpc.TransactionSignature
import foundation.metaplex.solana.transactions.SerializedTransaction
import foundation.metaplex.solana.transactions.Transaction
import souldestroyer.database.dao.WalletDAO
import souldestroyer.database.entity.WfWallet
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.bitcoinj.core.Base58
import souldestroyer.SoulDestroyer
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.LogRepository
import souldestroyer.sol.HotSigner
import souldestroyer.sol.Transactioneer
import kotlin.system.measureTimeMillis

class WalletImpl(
    val keypair: SolKeypair,
    val tag: String,
) {
    private val logRepo = LogRepository.instance()

    private val walletRepository: WalletRepository = WalletRepository.get()
    private val walletDAO: WalletDAO = walletRepository.walletDAO
    private val wfSolana: WfSolana = SoulDestroyer.instance().solana

    private val walletScope = CoroutineScope(Dispatchers.IO) + SupervisorJob()
    private var walletFlowJob: Job? = null

    private var wfWallet: WfWallet
    var publicKey: PublicKey

    private var balance: MutableState<Double> = mutableDoubleStateOf(-99999.0)

    private var signer: HotSigner

    companion object {
        fun fromDatabaseWfWallet(wfWallet: WfWallet): WalletImpl {
            val keypair = SolKeypair.fromByteArray(wfWallet.privateKey)
            val secret = Base58.encode(wfWallet.privateKey)
            return WalletImpl(keypair, wfWallet.tag)
        }
    }

    init {
        logRepo.log(
            message = "Instatiating Wallet Implementation:\n" +
                    "PublicKey: ${keypair.publicKey}\n" +
                    "PrivateKey: ${if (keypair.secretKey.isNotEmpty()) "Exists." else "Empty!"}",
            type = LogEntryType.INFO
        )
        wfWallet = WfWallet(
            publicKey = keypair.publicKey.toString(),
            privateKey = keypair.secretKey,
            tag = tag
        )
        publicKey = keypair.publicKey
        insertToDatabaseIfNotExists(wfWallet) {
            startWalletFlow(keypair.publicKey.toString())
            retrieveBalance()
        }
        Wallets.get().wList.add(this)
        signer = HotSigner(keypair)
    }

    override fun toString(): String {
        return "WALLET \"$tag\" (${publicKey}): ${balance.value}"
    }

    private fun insertToDatabaseIfNotExists(wfWallet: WfWallet, onCompletion: () -> Unit) {
        walletScope.launch(Dispatchers.IO) {
            if (!walletRepository.doesWalletExistInDB(wfWallet.publicKey)) {
                walletRepository.addWallet(wfWallet)
                logRepo.logSuccess("Added $wfWallet to database.")
            }
        }.invokeOnCompletion {
            onCompletion()
        }
    }

    private fun startWalletFlow(
        publicKeyString: String
    ) {
        walletFlowJob = walletScope.launch {
            walletDAO.loadWallet(publicKeyString)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect {
                    balance.value = it.balance
                }
        }
    }

    fun retrieveBalance(): Job {
        return walletScope.launch(Dispatchers.IO) {
            try {
                val balance =
                    wfSolana.rpc
                        .getBalance(publicKey, null) / Constants.LAMPERTS_PER_SOL

                walletScope.launch(Dispatchers.IO) {
                    walletRepository.walletDAO.updateBalance(publicKey.toString(), balance)
                }.invokeOnCompletion {
                    logRepo.logInfo("Updated balance of $tag: $balance")
                }
            } catch (e: Throwable) {
                // todo: add log, handle that stuff
                throw e
            }
        }
    }

    fun sendMemoInitTransaction() {
        val memo = "SoulDestroyer initialized Wallet $publicKey"

        logRepo.log(
            message = "Launching Memo Transaction.\nBuilding Tx...",
            type = LogEntryType.INFO
        )

        walletScope.launch {
            val totalTimeMs = measureTimeMillis {
                val transactionSignature: TransactionSignature
                val transaction: Transaction
                val serializedTransaction: SerializedTransaction

                val buildTimeMs = measureTimeMillis {
                    transaction = Transactioneer.buildMemoTransaction(
                        memo = memo,
                        signer = signer
                    )
                }

                val serializeTimeMs = measureTimeMillis {
                    serializedTransaction = transaction.serialize()
                }

                logRepo.log(
                    message = "Memo Tx was built ($buildTimeMs ms) and serialized ($serializeTimeMs ms).\n" +
                            "Sending transaction, waiting for signature from RPC...",
                    type = LogEntryType.INFO
                )

                val sendTimeMs = measureTimeMillis {
                    transactionSignature = wfSolana.rpc.sendTransaction(serializedTransaction, null)
                }

                val signatureResponseString = transactionSignature.decodeToString()

                logRepo.logSuccess(
                    message = "(Tx total ms to signature: ${buildTimeMs + serializeTimeMs + sendTimeMs} ms)\n" +
                            "Memo Tx signature received ($sendTimeMs ms):\n\n" +
                            signatureResponseString
                )
            }
        }
    }

    fun remove() {
        walletScope.launch {
            // Cancel wallet flow
            walletFlowJob?.cancelAndJoin()
        }.invokeOnCompletion {
            // Clear from database and Wallets list
            val walletsList = Wallets.get().wList
            val wallet = walletsList.firstOrNull { it.tag == tag }
            if (wallet == null) {
                logRepo.logError("Wallet with tag \"$tag\" not found.")
                return@invokeOnCompletion
            }
            walletScope.launch {
                var deleteFromDbSuccessful = false
                try {
                    walletRepository.walletDAO.delete(wfWallet)
                    deleteFromDbSuccessful = true
                } catch (e: Throwable) {
                    logRepo.logError("Could not remove WfWallet with tag $tag from Database. " + e.message)
                }
                if (deleteFromDbSuccessful) {
                    try {
                        walletsList.remove(wallet)
                    } catch (e: Throwable) {
                        logRepo.logError("Could not remove wallet with tag $tag from wallet list state holder. " + e.message)
                    }
                }
            }
        }
    }

    fun requestAirdrop() {

    }
}