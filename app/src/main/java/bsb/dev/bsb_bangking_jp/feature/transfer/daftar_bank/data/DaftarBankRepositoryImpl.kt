// feature/transfer/data/DaftarBankRepositoryImpl.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.daftar_bank.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.network.ApiErrorParser
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.NetworkErrorMapper
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.core.util.retry
import bsb.dev.bsb_bangking_jp.feature.transfer.daftar_bank.domain.BankItem
import bsb.dev.bsb_bangking_jp.feature.transfer.daftar_bank.domain.DaftarBankRepository

private const val SUCCESS_CODE = "0000"

class DaftarBankRepositoryImpl(
    private val api: DaftarBankApiService,
    private val secureStorage: SecureStorageService,
    private val localStore: DaftarBankLocalStore,
) : DaftarBankRepository {

    // 🔹 Cache in-memory -- hindari baca DataStore berulang kali selama 1 sesi app hidup.
    private var memoryCache: List<BankItem>? = null

    override suspend fun getDaftarBank(forceRefresh: Boolean): List<BankItem> {
        if (!forceRefresh) {
            memoryCache?.let { return it }

            val cachedData = localStore.get()
            val lastFetch = localStore.getLastFetchTime()
            val hasValidData = !cachedData.isNullOrEmpty()

            if (hasValidData && !localStore.isExpired(lastFetch)) {
                memoryCache = cachedData
                return cachedData!!
            }
        }

        val fresh = retry { fetchDaftarBank() }
        memoryCache = fresh
        localStore.save(fresh)
        return fresh
    }

    private suspend fun fetchDaftarBank(): List<BankItem> {
        try {
            val privateKey = secureStorage.getPrivateKey()
                ?: throw IllegalStateException("Private key tidak ditemukan, device belum ter-init.")

            val timestamp = ApiHeaders.currentTimestamp()
            val signature = SignatureUtils.sign(emptyMap<String, String>(), timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.getDaftarBank(
                headers = headers,
                tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
            )

            if (!response.isSuccessful) {
                throw ApiErrorParser.parse(response)
            }

            val body = response.body()
            if (body?.respCode != SUCCESS_CODE) {
                throw ApiException(body?.respCode, body?.respMessage ?: "Gagal memuat daftar bank.")
            }

            return body.data.map { it.toDomain() }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(null, NetworkErrorMapper.toUserMessage(e))
        }
    }
}