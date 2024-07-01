package souldestroyer.net

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Requester (
) {
    private val networkScope = CoroutineScope(Dispatchers.IO)
    private val httpClient = HttpClient()

    fun measurePingLatency() {
        networkScope.launch {
            /*val mediaType = "application/json".toMediaType()
            val jsonString = buildJsonObject {
                put("type", "ping")
                put("message", "")
            }.toString()

            val url = "http://10.0.2.2:8001"
            val body = jsonString.toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            var responseMessage = ""

            try {
                val latency = measureTimeMillis {
                    logface.log(
                        message = "Sending ping to $url",
                        type = LogEntryType.INFO
                    )
                    val response = httpClient.newCall(request).execute()
                    responseMessage = response.body!!.string()
                }
                logface.log(
                    message = "PONG Response in $latency ms. Message: $responseMessage",
                    type = LogEntryType.INFO
                )
            } catch (e: Throwable) {
                logface.log(
                    message = "PING Failure: ${e.message}",
                    type = souldestroyer.logs.LogEntryType.ERROR
                )
            }*/
        }
    }
}