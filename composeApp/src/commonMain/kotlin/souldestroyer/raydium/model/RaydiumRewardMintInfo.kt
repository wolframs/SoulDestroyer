package souldestroyer.raydium.model

import kotlinx.serialization.Serializable

@Serializable
data class RaydiumRewardMintInfo(
    val chainId: Int,
    val address: String,
    val programId: String,
    val logoURI: String,
    val symbol: String,
    val name: String,
    val decimals: Int
)