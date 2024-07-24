package souldestroyer.raydium.model

import kotlinx.serialization.Serializable

@Serializable
data class RaydiumPoolWeekStats(
    val volume: Double,
    val volumeQuote: Double,
    val volumeFee: Double,
    val apr: Double,
    val feeApr: Double,
    val priceMin: Double,
    val priceMax: Double,
    val rewardApr: List<Double>
)
