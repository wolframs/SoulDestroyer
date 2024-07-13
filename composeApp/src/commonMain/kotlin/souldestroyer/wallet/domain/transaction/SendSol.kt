package souldestroyer.wallet.domain.transaction

import foundation.metaplex.solana.transactions.TransactionSignature
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.launch
import souldestroyer.Constants
import souldestroyer.SoulDestroyer
import souldestroyer.logs.LogRepository
import souldestroyer.sol.Transactioneer
import souldestroyer.sol.WfSolana
import souldestroyer.sol.domain.checkTransactionStatus
import souldestroyer.wallet.WalletImpl
import souldestroyer.wallet.domain.WalletManager.walletScope
import kotlin.system.measureTimeMillis


fun WalletImpl.sendSolToReceiver(
    solAmount: Double,
    receiverPublicKey: String,
    logRepo: LogRepository = LogRepository.instance(),
    wfSolana: WfSolana = SoulDestroyer.instance().solana
) {
    logRepo.logDebug(
        message = "Launching transfer of $solAmount SOL to $receiverPublicKey from Wallet $tag."
    )

    walletScope.launch {
        try {
            val timeStart = System.currentTimeMillis()
            val amount = (solAmount * Constants.LAMPERTS_PER_SOL).toLong()
            val recPublicKey = PublicKey(receiverPublicKey)

            var transactionSignature: TransactionSignature? = null

            val transferTransaction = Transactioneer.buildTransferSolTransaction(
                fromPublicKey = publicKey,
                toPublicKey = recPublicKey,
                amount = amount,
                signer = signer
            )

            val buildTimeMs = System.currentTimeMillis() - timeStart

            val serializedTransaction = transferTransaction.serialize()

            val serializeTimeMs = System.currentTimeMillis() - buildTimeMs - timeStart

            logRepo.logTransactInfo(
                message = "Sending $solAmount SOL to $receiverPublicKey from Wallet $tag.\n" +
                        "Tx built, serialized. Sending and waiting for signature...",
                keys = listOf("Built in", "Serialized in"),
                values = listOf("$buildTimeMs ms", "$serializeTimeMs ms")
            )

            val sendTimeMs = measureTimeMillis {
                try {
                    transactionSignature = wfSolana.rpc.sendTransaction(serializedTransaction, null)
                } catch (transactionException: Throwable) {
                    logRepo.logError(
                        message = "Transaction error:\n\n" +
                                (transactionException.message
                                    ?: "Unknown exception (RPC.sendTransaction() did not return error data).")
                    )
                }
            }

            val signatureResponseString = transactionSignature?.decodeToString()

            val totalTimeMs = System.currentTimeMillis() - timeStart

            signatureResponseString?.let {
                logRepo.logTransactSuccess(
                    message = "Transaction to transfer $solAmount SOL to $receiverPublicKey from Wallet $tag was successful.\n" +
                            "Waiting for signature status to finalize...",
                    keys = listOf("Signature", "Response time", "Tx time total"),
                    values = listOf(
                        signatureResponseString,
                        "$sendTimeMs ms",
                        "$totalTimeMs ms"
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

        } catch (e: Throwable) {
            logRepo.logError(
                message = "Sending $solAmount SOL to $receiverPublicKey from Wallet $tag failed:\n\n${e.message ?: "Unknown error."}"
            )
        }
    }
}