package souldestroyer.sol.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SignatureStatus(
    val confirmationStatus: String?,
    val confirmations: Long?,
    val err: JsonElement?,
    val slot: Long,
    val status: JsonElement?
)
