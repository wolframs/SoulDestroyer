package souldestroyer.raydium.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class RaydiumMintInfo(
    val chainId: Int,
    val address: String,
    val programId: String,
    val logoURI: String,
    val symbol: String,
    val name: String,
    val decimals: Int,
    val tags: List<String>,
    val extensions: JsonObject = JsonObject(emptyMap()) // Can't be sure of all possible extensions, hence we do not bother
)
