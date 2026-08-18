// core/network/NetworkErrorMapper.kt
package bsb.dev.bsb_bangking_jp.core.network

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

 //Mapping exception jaringan -> pesan aman untuk user.

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
            throwable.respMessage // sudah pesan dari server, aman ditampilkan

        // 🔒 Tangkap SEMUA IOException lain (mis. "unexpected end of stream",
        // ProtocolException, EOFException, dll) SEBELUM jatuh ke else -- pesan
        // aslinya sering menyertakan URL/host, jadi jangan pernah diteruskan.
        is IOException ->
            "Koneksi terputus saat menghubungi server. Silakan coba lagi."

        // 🔒 Exception tak dikenal lainnya -- tetap pakai pesan generik,
        // JANGAN throwable.message (berpotensi bocorkan detail internal).
        else ->
            "Terjadi kesalahan. Silakan coba kembali."
    }
}