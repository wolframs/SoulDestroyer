package souldestroyer.raydium.model

import kotlinx.serialization.Serializable
import souldestroyer.raydium.serialization.RaydiumPoolSerializer

@Serializable(with = RaydiumPoolSerializer::class)
data class RaydiumPool(
    val type: String,
    val programId: String,
    val id: String,
    val mintA: RaydiumMintInfo,
    val mintB: RaydiumMintInfo,
    val price: Double,
    val mintAmountA: Double,
    val mintAmountB: Double,
    val feeRate: Double,
    val openTime: String,
    val tvl: Double,
    val day: RaydiumPoolDayStats,
    val week: RaydiumPoolWeekStats,
    val month: RaydiumPoolMonthStats,
    val pooltype: List<String>,
    val raydiumRewardDefaultInfos: List<RaydiumRewardDefaultInfo> = emptyList(),  // Use the simplified RewardDefaultInfo class
    val farmUpcomingCount: Int,
    val farmOngoingCount: Int,
    val farmFinishedCount: Int,
    val marketId: String?, // Make marketId optional
    val lpMint: RaydiumMintInfo,
    val lpPrice: Double,
    val lpAmount: Double
)
