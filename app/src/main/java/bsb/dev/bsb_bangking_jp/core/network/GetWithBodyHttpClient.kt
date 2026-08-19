package bsb.dev.bsb_bangking_jp.core.network

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object GetWithBodyHttpClient {
    private val gson = Gson()
    private const val TAG = "GetWithBodyHttpClient"

    data class Result(val statusCode: Int, val rawBody: String)

    suspend fun getWithBody(
        url: String,
        headers: Map<String, String>,
        body: Any,
        connectTimeoutMs: Int = 30_000,
        readTimeoutMs: Int = 30_000,
    ): Result = withContext(Dispatchers.IO) {
        val jsonBody = gson.toJson(body)
        val bodyBytes = jsonBody.toByteArray(StandardCharsets.UTF_8)

        // 🔹 Log request lengkap -- HttpURLConnection tidak lewat HttpLoggingInterceptor,
        // jadi ini satu-satunya cara melihat apa yang benar-benar dikirim.
        Log.d(TAG, "--> GET $url")
        headers.forEach { (key, value) -> Log.d(TAG, "$key: $value") }
        Log.d(TAG, "body: $jsonBody")

        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            doOutput = true
            doInput = true
            connectTimeout = connectTimeoutMs
            readTimeout = readTimeoutMs
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Content-Length", bodyBytes.size.toString())
            headers.forEach { (key, value) -> setRequestProperty(key, value) }
        }

        try {
            connection.outputStream.use { it.write(bodyBytes) }

            val statusCode = connection.responseCode
            val stream = if (statusCode in 200..299) connection.inputStream else connection.errorStream
            val rawBody = stream?.bufferedReader(StandardCharsets.UTF_8)?.use { it.readText() }.orEmpty()

            Log.d(TAG, "<-- $statusCode $url")
            Log.d(TAG, "response headers: ${connection.headerFields}")
            Log.d(TAG, "response body: $rawBody")

            Result(statusCode = statusCode, rawBody = rawBody)
        } catch (e: Exception) {
            Log.e(TAG, "request FAILED", e)
            throw e
        } finally {
            connection.disconnect()
        }
    }
}