// core/network/NetworkErrorMapper.kt
package bsb.dev.bsb_bangking_jp.core.network

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 ketika gagal menyambungkan ke server
 */
object NetworkErrorMapper {

    fun toUserMessage(throwable: Throwable): String = when (throwable) {
        is UnknownHostException ->
            "Tidak dapat terhubung ke server. Periksa koneksi internet Anda."

        is ConnectException ->
            "Gagal terhubung ke server. Pastikan koneksi internet Anda stabil, lalu coba lagi."

        is SocketTimeoutException ->
            "Koneksi ke server terlalu lama merespons. Silakan coba lagi."

        is SSLHandshakeException ->
            "Koneksi tidak aman terdeteksi. Silakan coba lagi nanti."

        is ApiException ->
            throwable.respMessage // sudah pesan dari server, tidak perlu diubah

        else ->
            throwable.message ?: "Terjadi kesalahan. Silakan coba kembali."
    }
}