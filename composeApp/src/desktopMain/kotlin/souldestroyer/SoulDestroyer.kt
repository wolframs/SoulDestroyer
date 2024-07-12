package souldestroyer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import souldestroyer.logs.LogRepository
import souldestroyer.shared.LimitedQueue
import souldestroyer.sol.WfSolana
import souldestroyer.wallet.Wallets

class SoulDestroyer(
    val solana: WfSolana = WfSolana.instance(),
    val wallets: Wallets = Wallets.instance(), // needs to be instantiated asap
    val soulScope: CoroutineScope = CoroutineScope(Dispatchers.Default) + SupervisorJob()
) {
    private val blockhashUpdateDelay = Constants.BLOCKHASH_REFRESH_DELAY
    private val logRepo = LogRepository.instance()

    var timeToBlockhashFlow = MutableStateFlow(0L)
    private var lastTenTimeToBlockhashValues = LimitedQueue<Long>(10)
    var timeToBlockhashAverageFlow = MutableStateFlow(0L)

    companion object {
        @Volatile
        private var INSTANCE: SoulDestroyer? = null

        fun instance(): SoulDestroyer {
            return INSTANCE ?: synchronized(this) {
                val instance = SoulDestroyer()
                INSTANCE = instance
                instance.startPeriodicBlockhashUpdate()
                instance
            }
        }
    }

    init {
        logRepo.logWarning(
            "SoulDestroyer is still inherently INSECURE.\n" +
                    "\n" +
                    "Wallet secrets are not stored in encrypted form and are held in a local database on your machine. " +
                    "Someone who can get a hold of said database could extract the secrets.\n" +
                    "\n" +
                    "Do not store substantial amounts of SOL in any wallet which you add to the wallet list in this app."
        )
    }

    private fun startPeriodicBlockhashUpdate() {
        soulScope.launch {
            while (isActive) {
                try {
                    timeToBlockhashFlow.value = solana.updateRecentBlockhash()
                    lastTenTimeToBlockhashValues.add(timeToBlockhashFlow.value)
                    timeToBlockhashAverageFlow.value = lastTenTimeToBlockhashValues.toList().average().toLong()
                } catch (e: Exception) {
                    logRepo.logError(
                        message = "Failed to update blockhash: ${e.message}\n\n" +
                                "Next attempt in $blockhashUpdateDelay ms."
                    )
                }
                delay(blockhashUpdateDelay)
            }
        }.invokeOnCompletion {
            logRepo.logWarning(
                message = "Periodic update of latest blockhash has terminated.\n" +
                        "App restart may be required."
            )
        }
    }
}