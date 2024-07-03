package souldestroyer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import souldestroyer.logs.LogRepository
import souldestroyer.sol.WfSolana
import souldestroyer.wallet.Wallets

class SoulDestroyer(
    val solana: WfSolana = WfSolana.get(),
    val wallets: Wallets = Wallets.instance(),
    val soulScope: CoroutineScope = CoroutineScope(Dispatchers.Default) + SupervisorJob()
) {
    companion object {
        @Volatile
        private var INSTANCE: SoulDestroyer? = null

        fun instance(): SoulDestroyer {
            return INSTANCE ?: synchronized(this) {
                val instance = SoulDestroyer()
                INSTANCE = instance
                instance
            }
        }
    }

    init {
        LogRepository.instance().logWarning(
            "SoulDestroyer is still inherently INSECURE.\n" +
                    "\n" +
                    "Wallet secrets are not stored in encrypted form and are held in a local database on your machine. " +
                    "Someone who can get a hold of said database could extract the secrets.\n" +
                    "\n" +
                    "Do not store substantial amounts of SOL in any wallet which you add to the wallet list in this app."
        )
    }
}