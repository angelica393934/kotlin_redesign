package bsb.dev.bsb_bangking_jp.data.repository

import bsb.dev.bsb_bangking_jp.core.network.AuthApiService
import bsb.dev.bsb_bangking_jp.core.network.dto.ErrorResponse
import bsb.dev.bsb_bangking_jp.core.network.dto.LoginRequest
import bsb.dev.bsb_bangking_jp.core.network.dto.LoginResponse
import bsb.dev.bsb_bangking_jp.core.network.dto.MeResponse
import bsb.dev.bsb_bangking_jp.core.network.dto.RefreshRequest
import bsb.dev.bsb_bangking_jp.core.network.dto.RefreshResponse
import bsb.dev.bsb_bangking_jp.core.util.ApiResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository untuk fitur autentikasi. Membungkus pemanggilan API dan
 * mapping error jadi ApiResult yang seragam, supaya ViewModel/UseCase
 * tidak perlu tahu soal HttpException/IOException dari Retrofit.
 *
 * `api` diambil lewat DI (Koin) -- lihat core/di/NetworkModule.kt.
 */
class AuthRepository(
    private val api: AuthApiService,
) {

    private val gson = Gson()

    suspend fun login(
        username: String,
        password: String,
        expiresInMins: Int = 30,
    ): ApiResult<LoginResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.login(
                LoginRequest(
                    username = username,
                    password = password,
                    expiresInMins = expiresInMins,
                )
            )
            ApiResult.Success(response)
        } catch (e: HttpException) {
            ApiResult.Error(extractErrorMessage(e))
        } catch (e: IOException) {
            ApiResult.Error("Tidak ada koneksi internet, coba lagi")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Terjadi kesalahan tidak diketahui")
        }
    }

    /**
     * GET /auth/me -- dipakai buat refresh data profil (mis. pull-to-refresh
     * di BerandaPage). `accessToken` di sini token MENTAH (tanpa "Bearer "),
     * prefix-nya ditambahkan di sini supaya AuthApiService tetap bodoh.
     */
    suspend fun getMe(accessToken: String): ApiResult<MeResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMe("Bearer $accessToken")
            ApiResult.Success(response)
        } catch (e: HttpException) {
            ApiResult.Error(extractErrorMessage(e))
        } catch (e: IOException) {
            ApiResult.Error("Tidak ada koneksi internet, coba lagi")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Terjadi kesalahan tidak diketahui")
        }
    }

    /**
     * POST /auth/refresh -- tukar refreshToken lama dengan pasangan token baru.
     * Prosesnya disiapkan di sini, TAPI belum ada pemanggil dari ViewModel/UI
     * manapun -- itu memang disengaja, dipanggil nanti begitu dibutuhkan
     * (mis. saat accessToken expired / dapat 401 dari endpoint lain).
     */
    suspend fun refreshToken(
        refreshToken: String,
        expiresInMins: Int = 30,
    ): ApiResult<RefreshResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.refreshToken(
                RefreshRequest(refreshToken = refreshToken, expiresInMins = expiresInMins)
            )
            ApiResult.Success(response)
        } catch (e: HttpException) {
            ApiResult.Error(extractErrorMessage(e))
        } catch (e: IOException) {
            ApiResult.Error("Tidak ada koneksi internet, coba lagi")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Terjadi kesalahan tidak diketahui")
        }
    }

    /**
     * Ambil pesan error ASLI dari body response API, mis. "Invalid credentials".
     * Kalau body tidak bisa diparse (server error / format tak terduga),
     * baru pakai pesan generik berdasarkan kode HTTP sebagai fallback.
     */
    private fun extractErrorMessage(e: HttpException): String {
        val rawBody = e.response()?.errorBody()?.string()
        val parsedMessage = rawBody?.let {
            runCatching { gson.fromJson(it, ErrorResponse::class.java)?.message }
                .getOrNull()
        }

        return parsedMessage?.takeIf { it.isNotBlank() } ?: when (e.code()) {
            400, 401 -> "Username atau password salah"
            404 -> "Layanan tidak ditemukan"
            in 500..599 -> "Server sedang bermasalah, coba lagi nanti"
            else -> "Terjadi kesalahan (${e.code()})"
        }
    }
}
