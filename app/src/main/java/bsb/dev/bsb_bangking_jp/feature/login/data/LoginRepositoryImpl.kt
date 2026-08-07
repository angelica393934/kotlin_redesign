package bsb.dev.bsb_bangking_jp.feature.login.data

import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.network.ApiErrorParser
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.feature.login.domain.LoginRepository

private const val SUCCESS_CODE = "0000"

class LoginRepositoryImpl(
    private val api: LoginApiService,
    private val secureStorage: SecureStorageService,
) : LoginRepository {

    override suspend fun login(useridLogin: String, passcode: String): Result<Unit> {
        return try {
            val response = api.login(
                headers = ApiHeaders.full(),
                body = LoginRequest(useridLogin = useridLogin, passcode = passcode),
            )
            if (!response.isSuccessful) {
                return Result.failure(ApiErrorParser.parse(response))
            }

            val body = response.body()
            if (body?.respCode != SUCCESS_CODE) {
                // 🔹 HTTP 2xx tapi respCode bukan "0000" -- padanan cabang "success tapi respCode != 0000" di Dart
                return Result.failure(
                    ApiException(body?.respCode, body?.respMessage ?: "Login gagal.")
                )
            }

            val accessToken = body.data?.accessToken
                ?: return Result.failure(ApiException("9999", "Access token tidak ditemukan."))
            val refreshToken = body.data.refreshToken

            secureStorage.saveLoginAccessToken(accessToken)
            refreshToken?.let { secureStorage.saveLoginRefreshToken(it) }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}