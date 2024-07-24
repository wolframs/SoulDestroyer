package souldestroyer.raydium.model

import kotlinx.serialization.Serializable

@Serializable
data class RaydiumPoolsResponse(
    val id: String,
    val success: Boolean,
    val data: RaydiumPoolsResponseData
)