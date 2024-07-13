package souldestroyer.sol.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import souldestroyer.Constants
import souldestroyer.logs.LogRepository
import souldestroyer.sol.WfSolana
import souldestroyer.sol.model.ConfirmationStatus
import souldestroyer.sol.model.SignatureStatus
import souldestroyer.wallet.Wallets
import souldestroyer.wallet.domain.WalletManager

suspend fun checkTransactionStatus(
    transactionSignatureBase58: String,
    logRepo: LogRepository,
    wfSolana: WfSolana
) {
    withContext(Dispatchers.IO) {
        var confirmed = false
        var pollingTimeCounter = 0
        val id = transactionSignatureBase58.take(6)
        var status: SignatureStatus? = null
        var attemptNo = 0

        delay(350) // makes no sense to start polling before transaction had a chance to be processed

        logRepo.logInfo(
            message = "Starting to follow up on status of Tx signature $id... until it is finalized. " +
                    "(Enable verbose log entries to see individual check request.)"
        )

        while (!confirmed) {
            // Our delay starts at CONFIRM_POLLING_DELAY, then increase by 50ms for each iteration
            delay(Constants.CONFIRM_POLLING_DELAY + (attemptNo * 50L))
            attemptNo++

            pollingTimeCounter += Constants.CONFIRM_POLLING_DELAY.toInt()
            if (pollingTimeCounter >= Constants.CONFIRM_MAX_POLLING_TIME) {
                timeoutReachedWarning(logRepo, id, status)
                return@withContext
            }

            try {
                status = wfSolana.getSignatureStatuses(
                    signature = transactionSignatureBase58,
                    id = id
                )

                if (status != null && status.confirmationStatus == ConfirmationStatus.FINALIZED.description) {
                    confirmed = true
                    logRepo.logSuccess(
                        message = "Transaction $id... is finalized."
                    )
                    // Refresh wallet balances
                    Wallets.instance().wList.forEach { wallet ->
                        delay(300)
                        wallet.retrieveBalance()
                    }

                } else {
                    logRepo.logDebug(
                        message = "Current status of Tx signature $id is " +
                                "${status?.confirmationStatus ?: "Unknown (may be dropped or RPC calls exceeded)"}..."
                    )
                }
            } catch (e: Throwable) {
                logRepo.logError(
                    message = "Error checking status for transaction $transactionSignatureBase58:\n\n${e.message ?: "Unknown error."}"
                )
            }
        }
    }
}

private fun timeoutReachedWarning(
    logRepo: LogRepository,
    id: String,
    status: SignatureStatus?
) {
    logRepo.logWarning(
        message = "Timeout reached for checking status of transaction $id...\n" +
                "The status never reached the finalized stage. " +
                "The transaction may have been dropped or RPC calls were exceeded during checking.",
        keys = listOf("Last known status", "Timeout limit", "Signature"),
        values = listOf(
            status?.confirmationStatus ?: "Unknown",
            "${Constants.CONFIRM_MAX_POLLING_TIME} ms",
            id
        )
    )
}