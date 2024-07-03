package souldestroyer.wallet

import souldestroyer.Constants
import souldestroyer.sol.WfSolana
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import souldestroyer.database.dao.WalletDAO
import souldestroyer.wallet.model.WfWallet
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.bitcoinj.core.Base58
import souldestroyer.SoulDestroyer
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.LogRepository
import souldestroyer.sol.HotSigner
import souldestroyer.wallet.domain.WalletManager

class WalletImpl(
    val keypair: SolKeypair,
    val tag: String,
) {
    private val walletScope = WalletManager.walletScope
    private val logRepo = LogRepository.instance()

    private val walletRepository: WalletRepository = WalletRepository.instance()
    private val walletDAO: WalletDAO = walletRepository.walletDAO
    private val wfSolana: WfSolana = SoulDestroyer.instance().solana

    private var walletFlowJob: Job? = null

    var publicKey: PublicKey

    private var balance: MutableState<Double> = mutableDoubleStateOf(-99999.0)

    var signer: HotSigner

    companion object {
        fun fromDatabaseWfWallet(wfWallet: WfWallet): WalletImpl {
            val keypair = SolKeypair.fromByteArray(wfWallet.privateKey)
            val secret = Base58.encode(wfWallet.privateKey)
            return WalletImpl(keypair, wfWallet.tag)
        }
    }

    init {
        publicKey = keypair.publicKey
        signer = HotSigner(keypair)

        try {
            WalletManager.insertToDatabaseIfNotExists(keypair, tag) {
                startWalletFlow(keypair.publicKey.toString())
                retrieveBalance()
            }

            logRepo.logSuccess(
                message = "Initialized Wallet.",
                keys = listOf("Tag", "Public Key"),
                values = listOf(tag, keypair.publicKey.toString())
            )
        } catch (e: Throwable) {
            logRepo.logError(
                message = "Initializing wallet failed.",
                keys = listOf("Tag", "Public Key", "Error"),
                values = listOf(tag, keypair.publicKey.toString(), e.message ?: "Unknown error.")
            )
        }
    }

    override fun toString(): String {
        return "WALLET \"$tag\" (${publicKey}): ${balance.value}"
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

    fun remove() {
        walletScope.launch {
            // Cancel wallet flow
            walletFlowJob?.cancelAndJoin()
        }.invokeOnCompletion {
            // Clear from database and Wallets list
            val walletsList = Wallets.instance().wList
            val wallet = walletsList.firstOrNull { it.tag == tag }
            if (wallet == null) {
                logRepo.logError("Wallet with tag \"$tag\" not found.")
                return@invokeOnCompletion
            }
            walletScope.launch {
                var deleteFromDbSuccessful = false
                try {
                    walletRepository.walletDAO.delete(publicKey.toString())
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