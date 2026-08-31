package bsb.dev.bsb_bangking_jp.feature.news.data

import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.GetWithBodyApiHelper
import bsb.dev.bsb_bangking_jp.core.network.NetworkErrorMapper
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.util.retry
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsDetail
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsDetailRepository

private const val SUCCESS_CODE = "0000"
private const val TTL_MILLIS = 24 * 60 * 60 * 1000L

class NewsDetailRepositoryImpl(
    private val apiHelper: GetWithBodyApiHelper,
) : NewsDetailRepository, ClearableRepository {

    private val cache = mutableMapOf<Int, NewsDetail>()
    private val lastFetchTime = mutableMapOf<Int, Long>()

    override suspend fun getNewsDetail(id: Int, forceRefresh: Boolean): NewsDetail {
        if (!forceRefresh && cache.containsKey(id) && !shouldRefresh(id)) {
            return cache.getValue(id)
        }

        val fresh = retry(maxAttempt = 3) { fetchDetail(id) }
        cache[id] = fresh
        lastFetchTime[id] = System.currentTimeMillis()
        return fresh
    }

    private fun shouldRefresh(id: Int): Boolean {
        val last = lastFetchTime[id] ?: return true
        return System.currentTimeMillis() - last > TTL_MILLIS
    }

    private suspend fun fetchDetail(id: Int): NewsDetail {
        try {
            val response = apiHelper.execute(
                path = "v1/info/getnewsbyid",
                body = GetNewsByIdRequest(id = id),
                responseType = NewsDetailResponse::class.java,
            )

            if (response.respCode != SUCCESS_CODE) {
                throw ApiException(response.respCode, response.respMessage ?: "Gagal memuat detail berita.")
            }

            return response.data.toDomain()
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(null, NetworkErrorMapper.toUserMessage(e))
        }
    }

    override fun clear() {
        cache.clear()
        lastFetchTime.clear()
    }
}