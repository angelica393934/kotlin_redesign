package bsb.dev.bsb_bangking_jp.data.repository

import bsb.dev.bsb_bangking_jp.core.network.AuthApiService
import bsb.dev.bsb_bangking_jp.core.network.RetrofitClient
import bsb.dev.bsb_bangking_jp.core.network.dto.LoginRequest
import bsb.dev.bsb_bangking_jp.core.network.dto.LoginResponse
import bsb.dev.bsb_bangking_jp.core.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository untuk fitur autentikasi. Membungkus pemanggilan API dan
 * mapping error jadi ApiResult yang seragam, supaya ViewModel tidak perlu
 * tahu soal HttpException/IOException dari Retrofit.
 */
class AuthRepository(
    private val api: AuthApiService = RetrofitClient.authApi,
) {

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
            val message = when (e.code()) {
                400 -> "Username atau password tidak boleh kosong"
                401 -> "Username atau password salah"
                else -> "Terjadi kesalahan (${e.code()})"
            }
            ApiResult.Error(message)
        } catch (e: IOException) {
            ApiResult.Error("Tidak ada koneksi internet, coba lagi")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Terjadi kesalahan tidak diketahui")
        }
    }
}
