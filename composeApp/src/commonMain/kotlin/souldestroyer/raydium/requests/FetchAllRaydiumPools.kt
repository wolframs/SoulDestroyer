package souldestroyer.raydium.requests

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Value
import souldestroyer.logs.LogRepository
import souldestroyer.raydium.RAYDIUM_POOLS_INFO_ENDPOINT
import souldestroyer.raydium.model.RaydiumPool
import souldestroyer.raydium.model.RaydiumPoolsResponse
import souldestroyer.sol.WfSolana
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

@Serializable
data class PoolInfo(
    val poolTotalBase: Double,
    val poolTotalQuote: Double,
    val baseVaultBalance: Double,
    val quoteVaultBalance: Double,
    val baseTokensInOpenOrders: Double,
    val quoteTokensInOpenOrders: Double,
    val baseTokenDecimals: Int,
    val quoteTokenDecimals: Int,
    val totalLp: String,
    val addedLpAmount: Double
)

private val client = HttpClient() {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

fun fetchPoolInfo(rpcUrl: String): PoolInfo? {
    var poolInfo: PoolInfo? = null
    try {
        val callURL = "http://localhost:3000/parse-pool-info"
        val execTime = measureTimeMillis {

            poolInfo = runBlocking {
                try {
                    client.get(callURL) {
                        parameter("rpcUrl", rpcUrl)
                    }.body()
                } catch (e: Exception) {
                    println("Error fetching pool info: ${e.message}")
                    null
                }
            }

            println(poolInfo)
        }

        LogRepository.instance().logDebug(
            message = "Node.js call finished.",
            keys = listOf("Time", "Call URL"),
            values = listOf("$execTime ms", callURL),
            addToLogList = true
        )
    } catch (e: Exception) {
        LogRepository.instance().logError("Error fetching pool info: ${e.message}")
    }

    return poolInfo
}

fun executeCommand(command: String): String {
    val processBuilder = ProcessBuilder(command.split(" "))
    processBuilder.redirectErrorStream(true)
    val process = processBuilder.start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val output = StringBuilder()
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        output.append(line).append("\n")
    }
    process.waitFor()
    return output.toString()
}

fun commandExecTest() {
    val command = "node G:\\solana\\sd-raydium\\dist\\index.js ${WfSolana.instance().rpcEndpoint.url}"
    val execTime = measureTimeMillis {
        val output = executeCommand(command)
        println(output)
    }
    LogRepository.instance().logDebug(
        message = "Node.js command execution finished.",
        keys = listOf("Time", "Command"),
        values = listOf("$execTime ms", command),
        addToLogList = true
    )
}

fun jScriptEngineTest() {
    val path = Paths.get("G:\\solana\\sd-raydium\\dist\\index.js")
    val jsCode = Files.readString(path)

    val context = Context.newBuilder("js")
        .option("js.commonjs-require", "true")
        .option(
            "js.commonjs-require-cwd",
            "G:\\solana\\sd-raydium\\dist"
        )  // Set the current working directory for require
        .option("log.file", "G:\\solana\\sd-raydium\\polyglot.log")
        //.option("js.ecmascript-version", "12")
        .allowAllAccess(true)
        .build()

    try {
        context.eval(
            "js",
            """
                        var process = {
                            env: {},
                            version: '1.0.0',
                        };
                    """
        )
        context.eval("js", jsCode)

        val parsePoolInfo: Value = context.getBindings("js").getMember("parsePoolInfo")
        val result = parsePoolInfo.execute().`as`(Map::class.java)

        println(result)
    } catch (e: Throwable) {
        println("Error evaluating JS code: ${e.message}\n${e.printStackTrace()}")
    } finally {
        context.close()
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