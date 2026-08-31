package bsb.dev.bsb_bangking_jp.feature.news.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.network.ApiErrorParser
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.NetworkErrorMapper
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.feature.news.domain.AllNewsItem
import bsb.dev.bsb_bangking_jp.feature.news.domain.AllNewsRepository

private const val SUCCESS_CODE = "0000"

class AllNewsRepositoryImpl(
    private val api: NewsApiService,
    private val secureStorage: SecureStorageService,
) : AllNewsRepository, ClearableRepository {

    private var cache: List<AllNewsItem>? = null

    override suspend fun getAllNews(forceRefresh: Boolean): List<AllNewsItem> {
        if (!forceRefresh && cache != null) return cache!!

        try {
            val privateKey = secureStorage.getPrivateKey()
                ?: throw IllegalStateException("Private key tidak ditemukan, device belum ter-init.")

            val timestamp = ApiHeaders.currentTimestamp()
            val signature = SignatureUtils.sign(emptyMap<String, String>(), timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.getAllNews(
                headers = headers,
                tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
            )
            if (!response.isSuccessful) throw ApiErrorParser.parse(response)

            val body = response.body()
            if (body?.respCode != SUCCESS_CODE) {
                throw ApiException(body?.respCode, body?.respMessage ?: "Gagal memuat berita.")
            }

            val fresh = body.data.map { it.toDomain() }
            cache = fresh
            return fresh
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(null, NetworkErrorMapper.toUserMessage(e))
        }
    }

    override fun clear() {
        cache = null
    }
}