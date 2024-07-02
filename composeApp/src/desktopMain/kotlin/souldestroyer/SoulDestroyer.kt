package souldestroyer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import souldestroyer.sol.WfSolana
import souldestroyer.wallet.Wallets

class SoulDestroyer(
    val solana: WfSolana = WfSolana.get(),
    val wallets: Wallets = Wallets.get(),
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
}