// core/crypto/JwtUtils.kt
package bsb.dev.bsb_bangking_jp.core.crypto

import android.util.Base64
import com.google.gson.Gson

/** Decode payload JWT tanpa verifikasi -- cuma buat ambil klaim "challenge". */
object JwtUtils {
    private val gson = Gson()

    fun extractChallenge(jwt: String): String {
        val parts = jwt.split(".")
        require(parts.size >= 2) { "Format challenge token tidak valid." }
        val payloadJson = String(base64UrlDecode(parts[1]), Charsets.UTF_8)
        val map = gson.fromJson(payloadJson, Map::class.java)
        return map["challenge"] as? String
            ?: throw IllegalStateException("Field 'challenge' tidak ditemukan di token.")
    }

    private fun base64UrlDecode(input: String): ByteArray {
        var normalized = input.replace('-', '+').replace('_', '/')
        while (normalized.length % 4 != 0) normalized += "="
        return Base64.decode(normalized, Base64.NO_WRAP)
    }
}