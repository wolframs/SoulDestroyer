package souldestroyer.sol

import foundation.metaplex.rpc.Blockhash
import foundation.metaplex.rpc.RPC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import souldestroyer.logs.LogRepository
import souldestroyer.settings.SettingsManager
import kotlin.system.measureTimeMillis

class WfSolana(
    private val logRepository: LogRepository = LogRepository.instance()
) {
    val wfSolanaScope = CoroutineScope(Dispatchers.Default) + SupervisorJob()

    var rpcEndpoint = SettingsManager().rpcEndpoint
    var rpc = RPC(rpcEndpoint.url)

    lateinit var recentBlockhash: Blockhash

    companion object {
        @Volatile
        private var INSTANCE: WfSolana? = null

        fun instance(): WfSolana {
            return INSTANCE ?: synchronized(this) {
                val instance = WfSolana()
                INSTANCE = instance
                instance
            }
        }
    }

    init {
        val timeToBlockhash = runBlocking { // run this blocking, because we need the blockhash for operations as soon as the app has started
            return@runBlocking updateRecentBlockhash()
        }
        logRepository.logInfo(
            message = "Active Solana RPC Endpoint is ${rpcEndpoint.description}.",
            keys = listOf("1st blockhash init"),
            values = listOf("$timeToBlockhash ms")
        )
    }

    /** Returns time to get latest blockhash in milliseconds. */
    suspend fun updateRecentBlockhash(): Long {
        return measureTimeMillis {
            withContext(Dispatchers.IO) {
                recentBlockhash = rpc.getLatestBlockhash(null).blockhash
            }
        }
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