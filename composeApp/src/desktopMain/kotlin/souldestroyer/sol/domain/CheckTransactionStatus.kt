package souldestroyer.sol.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import souldestroyer.Constants
import souldestroyer.logs.LogRepository
import souldestroyer.sol.WfSolana
import souldestroyer.sol.model.ConfirmationStatus

suspend fun checkTransactionStatus(
    transactionSignatureBase58: String,
    logRepo: LogRepository,
    wfSolana: WfSolana
) {
    withContext(Dispatchers.IO) {
        var confirmed = false
        var pollingTimeCounter = 0
        val id = transactionSignatureBase58.take(6)

        while (!confirmed) {
            delay(Constants.CONFIRM_POLLING_DELAY)

            pollingTimeCounter += Constants.CONFIRM_POLLING_DELAY.toInt()
            if (pollingTimeCounter >= Constants.CONFIRM_MAX_POLLING_TIME)
                return@withContext

            try {
                val status = wfSolana.getSignatureStatuses(
                    signature = transactionSignatureBase58,
                    id = id
                )

                if (status != null && status.confirmationStatus == ConfirmationStatus.FINALIZED.description) {
                    confirmed = true
                    logRepo.logSuccess(
                        message = "Transaction $id... is finalized."
                    )
                } else {
                    logRepo.logInfo(
                        message = "Checking status of Tx signature $id...",
                        keys = listOf("Current Status"),
                        values = listOf(status?.confirmationStatus ?: "Unknown (may be dropped or RPC calls exceeded)")
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