package souldestroyer.sol.model

import kotlinx.serialization.Serializable

@Serializable
data class RpcResponseResult(
    val context: RpcResponseResultContext,
    val value: List<SignatureStatus?>
)

@Serializable
data class RpcResponseResultContext(
    val apiVersion: String,
    val slot: Long
)