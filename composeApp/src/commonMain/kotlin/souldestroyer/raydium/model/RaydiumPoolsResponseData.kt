package souldestroyer.raydium.model

import kotlinx.serialization.Serializable

@Serializable
data class RaydiumPoolsResponseData(
    val count: Int,
    val data: List<RaydiumPool>,
    val hasNextPage: Boolean
)
