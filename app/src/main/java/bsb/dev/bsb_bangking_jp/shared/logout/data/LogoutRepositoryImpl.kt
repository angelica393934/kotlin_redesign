package bsb.dev.bsb_bangking_jp.shared.logout.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.network.ApiErrorParser
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.NetworkErrorMapper
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.shared.logout.domain.LogoutRepository
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService


private const val SUCCESS_CODE = "0000"

class LogoutRepositoryImpl(
    private val api: LogoutApiService,
    private val secureStorage: SecureStorageService,
) : LogoutRepository {

    override suspend fun logout(): Result<Unit> {
        return try {
            val privateKey = secureStorage.getPrivateKey()
                ?: throw IllegalStateException("Private key tidak ditemukan, device belum ter-init.")

            val timestamp = ApiHeaders.currentTimestamp()
            val signature = SignatureUtils.sign(emptyMap<String, String>(), timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.logout(
                headers = headers,
                tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
            )
            if (!response.isSuccessful) {
                return Result.failure(ApiErrorParser.parse(response))
            }

            val body = response.body()
            if (body?.respCode != SUCCESS_CODE) {
                return Result.failure(
                    ApiException(body?.respCode, body?.respMessage ?: "Logout gagal.")
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(ApiException(null, NetworkErrorMapper.toUserMessage(e)))
        }
    }
}