package souldestroyer.sol

import foundation.metaplex.rpc.RPC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import souldestroyer.logs.LogRepository
import souldestroyer.settings.SettingsManager

class WfSolana(
    private val logRepository: LogRepository = LogRepository.instance()
) {
    var rpcEndpoint = SettingsManager().rpcEndpoint
    var rpc = RPC(rpcEndpoint.url)

    companion object {
        @Volatile
        private var INSTANCE: WfSolana? = null

        fun get(): WfSolana {
            return INSTANCE ?: synchronized(this) {
                val instance = WfSolana()
                INSTANCE = instance
                instance
            }
        }
    }

    init {
        logRepository.logInfo(
            message = "Active Solana RPC Endpoint is ${rpcEndpoint.description}."
        )
    }

    suspend fun changeEndpoint(newRPCEndpoint: RPCEndpoint): Boolean {
        return withContext(Dispatchers.Default) {
            return@withContext try {
                rpcEndpoint = newRPCEndpoint
                rpc = RPC(rpcEndpoint.url)
                SettingsManager().rpcEndpoint = newRPCEndpoint
                logRepository.logSuccess(
                    message = "Changed RPC Endpoint to ${rpcEndpoint.description}."
                )
                true
            } catch (e: Throwable) {
                logRepository.logError(
                    message = "Failed to change RPC endpoint. App restart may be required.\n" +
                            "${e.message}"
                )
                false
            }
        }
    }
}