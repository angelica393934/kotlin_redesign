package bsb.dev.bsb_bangking_jp.feature.news.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.network.ApiErrorParser
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.NetworkErrorMapper
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.util.retry
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsItem
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsRepository

private const val SUCCESS_CODE = "0000"
private const val TTL_MILLIS = 24 * 60 * 60 * 1000L

class NewsRepositoryImpl(
    private val api: NewsApiService,
    private val secureStorage: SecureStorageService,
) : NewsRepository, ClearableRepository {

    private var cache: List<NewsItem>? = null
    private var lastFetchTime: Long? = null

    override suspend fun getNews(forceRefresh: Boolean): List<NewsItem> {
        if (!forceRefresh && cache != null && !shouldRefresh()) {
            return cache!!
        }
        val fresh = retry{ fetchNews() }
        cache = fresh
        lastFetchTime = System.currentTimeMillis()
        return fresh
    }

    private fun shouldRefresh(): Boolean {
        val last = lastFetchTime ?: return true
        return System.currentTimeMillis() - last > TTL_MILLIS
    }

    private suspend fun fetchNews(): List<NewsItem> {
        try {
            val privateKey = secureStorage.getPrivateKey()
                ?: throw IllegalStateException("Private key tidak ditemukan, device belum ter-init.")

            val timestamp = ApiHeaders.currentTimestamp()
            val signature = SignatureUtils.sign(emptyMap<String, String>(), timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.getNews(headers = headers)
            if (!response.isSuccessful) throw ApiErrorParser.parse(response)

            val body = response.body()
            if (body?.respCode != SUCCESS_CODE) {
                throw ApiException(body?.respCode, body?.respMessage ?: "Gagal memuat berita.")
            }

            // padanan `.where((e) => e.hasImage)`
            return body.data.berita.map { it.toDomain() }.filter { it.hasImage }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(null, NetworkErrorMapper.toUserMessage(e))
        }
    }

    override fun clear() {
        cache = null
        lastFetchTime = null
    }
}