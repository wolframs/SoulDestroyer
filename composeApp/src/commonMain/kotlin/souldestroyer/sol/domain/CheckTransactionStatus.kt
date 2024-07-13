package souldestroyer.sol.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import souldestroyer.Constants
import souldestroyer.logs.LogRepository
import souldestroyer.sol.WfSolana
import souldestroyer.sol.model.ConfirmationStatus
import souldestroyer.sol.model.SignatureStatus
import souldestroyer.wallet.Wallets

suspend fun checkTransactionStatus(
    transactionSignatureBase58: String,
    logRepo: LogRepository,
    wfSolana: WfSolana
) {
    withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        var confirmed = false
        var pollingTimeCounter = 0L
        val id = transactionSignatureBase58.take(6)
        var status: SignatureStatus? = null

        delay(350) // makes no sense to start polling before transaction had a chance to be processed

        logRepo.logInfo(
            message = "Starting to follow up on status of Tx signature $id... until it is finalized. " +
                    "(Enable verbose log entries to see individual check requests.)"
        )

        var attemptNo = 0

        val baseDelay = 750L
        val maxDecreaseAttempts = 12
        val increaseAfterMaxDecreaseAttempts = 120L

        while (!confirmed) {
            val iterationDelay = if (attemptNo < maxDecreaseAttempts) {
                // Decrease delay from 2x baseDelay (1500) to baseDelay over maxDecreaseAttempts iterations
                // This will lead to the shortest delay of 750ms after 13.5 seconds
                (2 * baseDelay) - ((baseDelay / maxDecreaseAttempts) * attemptNo)
            } else {
                // After X attempts, increase delay for each attempt until exit due to CONFIRM_MAX_POLLING_TIME
                baseDelay + ((attemptNo - maxDecreaseAttempts) * increaseAfterMaxDecreaseAttempts)
            }

            delay(iterationDelay)
            attemptNo++

            pollingTimeCounter += iterationDelay
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
                    val duration = System.currentTimeMillis() - startTime
                    logRepo.logSuccess(
                        message = "Transaction $id... is finalized.",
                        keys = listOf("Duration", "Signature"),
                        values = listOf("$duration ms", transactionSignatureBase58)
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