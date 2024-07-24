package souldestroyer.raydium.model

import kotlinx.serialization.Serializable
import souldestroyer.raydium.serialization.RewardDefaultInfoSerializer

@Serializable(with = RewardDefaultInfoSerializer::class)
data class RaydiumRewardDefaultInfo(
    val mint: RaydiumRewardMintInfo,
    val perSecond: String,
    val startTime: String?,
    val endTime: String?
)
