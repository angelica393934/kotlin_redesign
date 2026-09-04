package bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.network.ApiErrorParser
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.NetworkErrorMapper
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.util.retry
import bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.domain.SavedRecipientItem
import bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.domain.SavedRecipientRepository

private const val SUCCESS_CODE = "0000"
private const val EMPTY_DATA_CODE = "0860" // padanan cabang "empty data" di 

class SavedRecipientRepositoryImpl(
    private val api: SavedRecipientApiService,
    private val secureStorage: SecureStorageService,
) : SavedRecipientRepository, ClearableRepository {

    private var cache: List<SavedRecipientItem>? = null

    override val hasData: Boolean get() = cache != null
    override val cachedData: List<SavedRecipientItem>? get() = cache

    override suspend fun getSavedRecipients(forceRefresh: Boolean): List<SavedRecipientItem> {
        if (!forceRefresh && cache != null) {
            return cache!!
        }
        // 🔹 Padanan  "NO RETRY" cuma untuk get pertama kali dari Bloc,
        // tapi di layer repository kita tetap pakai retry (konsisten dgn repo lain di project ini).
        val fresh = retry { fetchSavedRecipients() }
        cache = fresh
        return fresh
    }

    private suspend fun fetchSavedRecipients(): List<SavedRecipientItem> {
        try {
            val privateKey = secureStorage.getPrivateKey()
                ?: throw IllegalStateException("Private key tidak ditemukan, device belum ter-init.")

            val timestamp = ApiHeaders.currentTimestamp()
            val signature = SignatureUtils.sign(emptyMap<String, String>(), timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.getSavedRecipients(
                headers = headers,
                tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
            )

            if (!response.isSuccessful) {
                throw ApiErrorParser.parse(response)
            }

            val body = response.body()
            return when (body?.respCode) {
                SUCCESS_CODE -> body.data.map { it.toDomain() }
                EMPTY_DATA_CODE -> emptyList()
                else -> throw ApiException(
                    body?.respCode,
                    body?.respMessage ?: "Gagal memuat daftar rekening tersimpan.",
                )
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(null, NetworkErrorMapper.toUserMessage(e))
        }
    }

    /** Padanan PUT /v1/dashboard/putsavedrecipients. */
    override suspend fun updateSavedRecipient(id: String, alias: String): Result<Unit> {
        return try {
            val privateKey = secureStorage.getPrivateKey()
                ?: return Result.failure(IllegalStateException("Private key tidak ditemukan."))

            val timestamp = ApiHeaders.currentTimestamp()
            val body = UpdateSavedRecipientRequest(id = id, alias = alias)
            val signature = SignatureUtils.sign(body, timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.updateSavedRecipient(
                headers = headers,
                body = body,
                tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
            )

            val respBody = response.body()
            val respCode = respBody?.respCode ?: "9999"
            val respMessage = respBody?.respMessage ?: "Terjadi kesalahan"

            if (!response.isSuccessful || respCode != SUCCESS_CODE) {
                return Result.failure(ApiException(respCode, respMessage))
            }

            // 🔹 Invalidate cache -- paksa fetch ulang biar alias baru ke-refresh.
            cache = null

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(ApiException("9999", e.message ?: "Jaringan bermasalah. Silahkan coba lagi."))
        }
    }

    /** Padanan DELETE /v1/dashboard/deletesavedrecipients. */
    override suspend fun deleteSavedRecipient(ids: List<String>): Result<Unit> {
        return try {
            val privateKey = secureStorage.getPrivateKey()
                ?: return Result.failure(IllegalStateException("Private key tidak ditemukan."))

            val timestamp = ApiHeaders.currentTimestamp()
            val body = DeleteSavedRecipientRequest(id = ids)
            val signature = SignatureUtils.sign(body, timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.deleteSavedRecipient(
                headers = headers,
                body = body,
                tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
            )

            val respBody = response.body()
            val respCode = respBody?.respCode ?: "9999"
            val respMessage = respBody?.respMessage ?: "Terjadi kesalahan"

            if (!response.isSuccessful || respCode != SUCCESS_CODE) {
                return Result.failure(ApiException(respCode, respMessage))
            }

            cache = null

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(ApiException("9999", e.message ?: "Jaringan bermasalah. Silahkan coba lagi."))
        }
    }

    override fun clear() {
        cache = null
    }
}