package souldestroyer.wallet.domain.transaction

import foundation.metaplex.solana.transactions.SerializedTransaction
import foundation.metaplex.solana.transactions.Transaction
import foundation.metaplex.solana.transactions.TransactionSignature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import souldestroyer.SoulDestroyer
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.LogRepository
import souldestroyer.sol.HotSigner
import souldestroyer.sol.Transactioneer
import souldestroyer.sol.WfSolana
import kotlin.system.measureTimeMillis

fun sendMemoTransaction(
    memoText: String,
    signer: HotSigner,
    logRepo: LogRepository = LogRepository.instance(),
    cScope: CoroutineScope = SoulDestroyer.instance().soulScope,
    wfSolana: WfSolana = SoulDestroyer.instance().solana,
) {
    logRepo.log(
        message = "Launching Memo Transaction.\nBuilding Tx...",
        type = LogEntryType.INFO
    )

    cScope.launch {
        val totalTimeMs = measureTimeMillis {
            val transactionSignature: TransactionSignature
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
                message = "Memo transaction successful.",
                keys = listOf("Signature", "Response time", "Tx time total"),
                values = listOf(signatureResponseString, "$sendTimeMs ms", "${buildTimeMs + serializeTimeMs + sendTimeMs} ms")
            )
        }
    }
}