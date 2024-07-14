package souldestroyer.wallet.domain.transaction

import foundation.metaplex.solana.transactions.SerializedTransaction
import foundation.metaplex.solana.transactions.Transaction
import foundation.metaplex.solana.transactions.TransactionSignature
import kotlinx.coroutines.launch
import souldestroyer.SoulDestroyer
import souldestroyer.logs.LogRepository
import souldestroyer.sol.Transactioneer
import souldestroyer.sol.WfSolana
import souldestroyer.sol.status.checkTransactionStatus
import souldestroyer.wallet.WalletImpl
import souldestroyer.wallet.domain.WalletManager.walletScope
import kotlin.system.measureTimeMillis

fun WalletImpl.sendMemoTransaction(
    memoText: String,
    logRepo: LogRepository = LogRepository.instance(),
    wfSolana: WfSolana = SoulDestroyer.instance().solana,
) {
    logRepo.logDebug(
        message = "Launching Memo Transaction.\nBuilding Tx...",
    )

    walletScope.launch {
        var transactionSignature: TransactionSignature? = null
        val transaction: Transaction
        val serializedTransaction: SerializedTransaction

        val buildTimeMs = measureTimeMillis {
            transaction = Transactioneer.buildMemoTransaction(
                memo = memoText,
                signer = signer
            )
        }

        val serializeTimeMs = measureTimeMillis {
            serializedTransaction = transaction.serialize()
        }

        logRepo.logTransactInfo(
            message = "Sending Memo Tx, waiting for signature from RPC...",
            keys = listOf("Built in", "Serialized in"),
            values = listOf(
                "$buildTimeMs ms",
                "$serializeTimeMs ms"
            )
        )

        val sendTimeMs = measureTimeMillis {
            try {
                transactionSignature = wfSolana.rpc.sendTransaction(serializedTransaction, null)
            } catch (transactionException: Throwable) {
                logRepo.logError(
                    message = "Memo transaction error:\n\n" +
                            (transactionException.message
                                ?: "Unknown exception (RPC.sendTransaction() did not return error data).")
                )
            }
        }

        val signatureResponseString = transactionSignature?.decodeToString()

        signatureResponseString?.let {
            logRepo.logTransactSuccess(
                message = "Memo transaction successful.",
                keys = listOf("Signature", "Response time", "Tx time total"),
                values = listOf(
                    signatureResponseString,
                    "$sendTimeMs ms",
                    "${buildTimeMs + serializeTimeMs + sendTimeMs} ms"
                )
            )

            // Start confirm status polling on global scope
            SoulDestroyer.instance().soulScope.launch {
                checkTransactionStatus(
                    transactionSignatureBase58 = signatureResponseString,
                    logRepo = logRepo,
                    wfSolana = wfSolana
                )
            }
        }
    }
}