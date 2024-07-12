package souldestroyer.sol.domain

import com.funkatronics.rpccore.Rpc20Response
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import souldestroyer.sol.WfSolana
import souldestroyer.sol.model.RpcResponseResult
import souldestroyer.sol.model.SignatureStatus

@OptIn(InternalAPI::class)
suspend fun WfSolana.getSignatureStatuses(
    signature: String,
    endpoint: String = rpcEndpoint.url,
    id: String
): SignatureStatus? {
    return withContext(Dispatchers.IO) {
        HttpClient().use { client ->
            val response: HttpResponse = client.post(endpoint) {
                contentType(ContentType.Application.Json)
                body = """
                        {
                            "jsonrpc": "2.0",
                            "id": "$id",
                            "method": "getSignatureStatuses",
                            "params": [["$signature"]]
                        }
                    """
            }
            val responseBody = response.bodyAsText()
            println("RPC Response: $responseBody") // Log the full response

            val json = Json { ignoreUnknownKeys = true }
            val parsedResponse: Rpc20Response<RpcResponseResult> = json.decodeFromString(responseBody)

            return@withContext parsedResponse.result?.value?.firstOrNull()
        }
    }
}