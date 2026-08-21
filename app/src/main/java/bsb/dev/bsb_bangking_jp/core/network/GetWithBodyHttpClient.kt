package bsb.dev.bsb_bangking_jp.core.network

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URI
import java.nio.charset.StandardCharsets
import javax.net.ssl.SSLSocketFactory

/**
 * Versi RAW SOCKET dari GetWithBodyHttpClient.
 *
 * MASALAH DENGAN HttpURLConnection:
 * Implementasi HttpURLConnection di Android (dibungkus OkHttp internal) tidak menjamin
 * method HTTP yang benar-benar dikirim ke server sama persis dengan yang di-set lewat
 * `requestMethod = "GET"` ketika doOutput=true -- untuk kombinasi non-standar (GET+body),
 * perilaku ini TIDAK TERDOKUMENTASI RESMI dan bisa berbeda antar versi Android/OEM.
 * Ini yang menyebabkan server membalas 405 Method Not Allowed padahal endpoint yang sama
 * sukses saat dites via Postman/curl (yang menulis request line HTTP secara eksplisit).
 *
 * SOLUSI: tulis HTTP request secara manual lewat Socket mentah, PERSIS seperti cara
 * curl/Postman bekerja di level bawah -- baris pertama request selalu
 * "GET /path HTTP/1.1" tanpa ada lapisan abstraksi yang bisa mengubahnya diam-diam.
 */
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

        val uri = URI(url)
        val isHttps = uri.scheme.equals("https", ignoreCase = true)
        val host = uri.host
        val port = if (uri.port != -1) uri.port else if (isHttps) 443 else 80
        val path = uri.rawPath + (uri.rawQuery?.let { "?$it" } ?: "")

        Log.d(TAG, "--> GET $url (raw socket)")
        headers.forEach { (key, value) -> Log.d(TAG, "$key: $value") }
        Log.d(TAG, "body: $jsonBody")

        val socket: Socket = if (isHttps) {
            (SSLSocketFactory.getDefault().createSocket() as Socket)
        } else {
            Socket()
        }

        try {
            socket.connect(InetSocketAddress(host, port), connectTimeoutMs)
            socket.soTimeout = readTimeoutMs

            // ---- Susun request line + header secara manual, byte demi byte ----
            val requestBuilder = StringBuilder()
            requestBuilder.append("GET $path HTTP/1.1\r\n")
            requestBuilder.append("Host: $host\r\n")
            requestBuilder.append("Content-Type: application/json\r\n")
            requestBuilder.append("Content-Length: ${bodyBytes.size}\r\n")
            requestBuilder.append("Connection: close\r\n")
            headers.forEach { (key, value) ->
                // Content-Type/Content-Length sudah ditulis manual di atas, skip duplikat
                if (!key.equals("Content-Type", true) && !key.equals("Content-Length", true)) {
                    requestBuilder.append("$key: $value\r\n")
                }
            }
            requestBuilder.append("\r\n")

            val outputStream = socket.getOutputStream()
            outputStream.write(requestBuilder.toString().toByteArray(StandardCharsets.UTF_8))
            outputStream.write(bodyBytes)
            outputStream.flush()

            // ---- Baca response ----
            val reader = BufferedReader(InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))

            val statusLine = reader.readLine() ?: throw java.io.IOException("Tidak ada response dari server.")
            val statusCode = statusLine.split(" ").getOrNull(1)?.toIntOrNull()
                ?: throw java.io.IOException("Status line tidak valid: $statusLine")

            var contentLength = -1
            var isChunked = false
            var line: String?
            while (true) {
                line = reader.readLine()
                if (line.isNullOrEmpty()) break
                val lower = line.lowercase()
                when {
                    lower.startsWith("content-length:") -> contentLength = line.substringAfter(":").trim().toIntOrNull() ?: -1
                    lower.startsWith("transfer-encoding:") && lower.contains("chunked") -> isChunked = true
                }
            }

            val rawBody = when {
                isChunked -> readChunkedBody(reader)
                contentLength >= 0 -> {
                    val buf = CharArray(contentLength)
                    var readTotal = 0
                    while (readTotal < contentLength) {
                        val n = reader.read(buf, readTotal, contentLength - readTotal)
                        if (n == -1) break
                        readTotal += n
                    }
                    String(buf, 0, readTotal)
                }
                else -> reader.readText()
            }

            Log.d(TAG, "<-- $statusCode $url")
            Log.d(TAG, "response body: $rawBody")

            Result(statusCode = statusCode, rawBody = rawBody)
        } catch (e: Exception) {
            Log.e(TAG, "request FAILED", e)
            throw e
        } finally {
            try { socket.close() } catch (e: Exception) { /* abaikan */ }
        }
    }

    private fun readChunkedBody(reader: BufferedReader): String {
        val sb = StringBuilder()
        while (true) {
            val sizeLine = reader.readLine() ?: break
            val size = sizeLine.trim().toIntOrNull(16) ?: break
            if (size == 0) break
            val buf = CharArray(size)
            var readTotal = 0
            while (readTotal < size) {
                val n = reader.read(buf, readTotal, size - readTotal)
                if (n == -1) break
                readTotal += n
            }
            sb.append(buf, 0, readTotal)
            reader.readLine() // baris kosong penutup chunk
        }
        return sb.toString()
    }

    /** Helper: baca sisa reader jadi String (fallback kalau tidak ada Content-Length/chunked). */
    private fun BufferedReader.readText(): String {
        val sb = StringBuilder()
        val buf = CharArray(4096)
        while (true) {
            val n = read(buf)
            if (n == -1) break
            sb.append(buf, 0, n)
        }
        return sb.toString()
    }
}