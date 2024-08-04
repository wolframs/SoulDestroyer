package souldestroyer.raydium.transaction

import kotlinx.serialization.Serializable

@Serializable
data class PoolInfo(
    val programId: String,
    val ammId: String,
    val ammAuthority: String,
    val ammOpenOrders: String,
    val ammTargetOrders: String,
    val poolCoinTokenAccount: String,
    val poolPcTokenAccount: String,
    val serumProgramId: String,
    val serumMarket: String,
    val serumBids: String,
    val serumAsks: String,
    val serumEventQueue: String,
    val serumCoinVaultAccount: String,
    val serumPcVaultAccount: String,
    val serumVaultSigner: String,
    val modelDataAccount: String? = null,
    val version: Int
)
