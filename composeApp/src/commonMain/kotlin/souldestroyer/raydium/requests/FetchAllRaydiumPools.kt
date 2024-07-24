package souldestroyer.raydium.requests

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import souldestroyer.logs.LogRepository
import souldestroyer.raydium.RAYDIUM_POOLS_INFO_ENDPOINT
import souldestroyer.raydium.model.RaydiumPool
import souldestroyer.raydium.model.RaydiumPoolsResponse

private val client = HttpClient() {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

suspend fun fetchAllRaydiumPools(
    limit: Int = 1000,
    logRepo: LogRepository = LogRepository.instance()
): List<RaydiumPool> {
    val allPools = mutableListOf<RaydiumPool>()
    var currentPage = 1
    var hasNextPage: Boolean

    try {
        do {
            logRepo.logDebug("Fetching Raydium pools for page $currentPage...")

            val response: RaydiumPoolsResponse = client.get(RAYDIUM_POOLS_INFO_ENDPOINT) {
                parameter("poolType", "standard")
                parameter("poolSortField", "default")
                parameter("sortType", "desc")
                parameter("pageSize", limit)
                parameter("page", currentPage)
            }.body()

            logRepo.logDebug(message = "\n\n--- RaydiumPoolsResponse ---\n$response\n\n", addToLogList = false)

            logRepo.logDebug("Response list length of Raydium pools: ${response.data.data.size}")

            if (response.success) {
                logRepo.logDebug(
                    message = response.data.data.joinToString("\n") { pool ->
                        "Pair: ${pool.id}, mintA: ${pool.mintA.symbol}, mintB: ${pool.mintB.symbol}"
                    },
                    addToLogList = false
                )
                allPools.addAll(response.data.data)
                hasNextPage = response.data.hasNextPage
                currentPage++
            } else {
                logRepo.logError("Failed to fetch Raydium pools. ResponseId: ${response.id}")
                hasNextPage = false
            }
        } while (hasNextPage)
    } catch (e: Exception) {
        logRepo.logError("Error fetching Raydium pools: ${e.message}")
    }

    return allPools
}