package souldestroyer.wallet.domain.transaction

import foundation.metaplex.amount.createAmount
import foundation.metaplex.base58.encodeToBase58String
import foundation.metaplex.rpc.RpcRequestAirdropConfiguration
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import souldestroyer.Constants
import souldestroyer.SoulDestroyer
import souldestroyer.logs.LogRepository
import souldestroyer.sol.HotSigner
import souldestroyer.sol.WfSolana
import souldestroyer.sol.domain.checkTransactionStatus
import souldestroyer.wallet.WalletImpl
import souldestroyer.wallet.domain.WalletManager.walletScope

fun WalletImpl.sendAirdropRequest(
    solAmountAsDouble: Double,
    logRepo: LogRepository = LogRepository.instance(),
    wfSolana: WfSolana = SoulDestroyer.instance().solana,
) {
    walletScope.launch(Dispatchers.IO) {
        logRepo.logTransactInfo(
            message = "Requesting airdrop for $tag.\nWaiting for signature...",
        )

        try {
            val amount = createAmount((solAmountAsDouble * Constants.LAMPERTS_PER_SOL).toInt(), "SOL", 9)
            val signature = wfSolana.rpc.requestAirdrop(
                RpcRequestAirdropConfiguration(
                    publicKey = publicKey,
                    lamports = amount
                )
            )

            logRepo.logTransactSuccess(
                message = "Airdrop request succeeded.",
                keys = listOf("Tx Signature"),
                values = listOf(signature.decodeToString())
            )

            // Start confirm status polling on global scope
            SoulDestroyer.instance().soulScope.launch {
                checkTransactionStatus(
                    transactionSignatureBase58 = signature.encodeToBase58String(),
                    logRepo = logRepo,
                    wfSolana = wfSolana
                )
            }
        } catch (e: Throwable) {
            logRepo.logError(
                message = "Requesting airdrop for $tag failed:\n\n${e.message ?: "Unknown error."}"
            )
        }
    }
}