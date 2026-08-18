package bsb.dev.bsb_bangking_jp.feature.aktivitas.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.network.ApiErrorParser
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.util.retry
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityFilterPayload
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityHistoryRepository

private const val SUCCESS_CODE = "0000"
private const val LIMIT = 10

class ActivityHistoryRepositoryImpl(
    private val api: ActivityHistoryApiService,
    private val secureStorage: SecureStorageService,
) : ActivityHistoryRepository, ClearableRepository {

    private val _items = mutableListOf<HistoryItem>()
    private var offset = 0
    private var _hasMore = true

    override val hasMore: Boolean get() = _hasMore
    override val items: List<HistoryItem> get() = _items.toList()

    override suspend fun loadInitial(
        accountNumber: String,
        filter: ActivityFilterPayload,
    ): List<HistoryItem> {
        reset()
        val fresh = retry(maxAttempt = 3) { fetchHistory(accountNumber, filter) }
        _items.addAll(fresh)
        _hasMore = fresh.size == LIMIT
        offset += LIMIT
        return items
    }

    override suspend fun loadMore(
        accountNumber: String,
        filter: ActivityFilterPayload,
    ): List<HistoryItem> {
        if (!_hasMore) return items

        val fresh = fetchHistory(accountNumber, filter)
        _items.addAll(fresh)
        _hasMore = fresh.size == LIMIT
        offset += LIMIT
        return items
    }

    private suspend fun fetchHistory(
        accountNumber: String,
        filter: ActivityFilterPayload,
    ): List<HistoryItem> {
        val privateKey = secureStorage.getPrivateKey()
            ?: throw IllegalStateException("Private key tidak ditemukan, device belum ter-init.")

        val body = GetHistoryRequest(
            accountNumber = accountNumber,
            limit = LIMIT,
            offset = offset,
            fromDateTime = filter.fromDate,
            toDateTime = filter.toDate,
            quickRange = filter.quickRange,
            jenis = filter.jenis?.takeIf { it.isNotEmpty() },
            category = filter.category?.takeIf { it.isNotEmpty() },
        )

        val timestamp = ApiHeaders.currentTimestamp()
        val signature = SignatureUtils.sign(body, timestamp, privateKey)
        val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

        val response = api.getHistory(
            headers = headers,
            body = body,
            tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
        )

        if (!response.isSuccessful) {
            throw ApiErrorParser.parse(response)
        }

        val respBody = response.body()
        if (respBody?.respCode != SUCCESS_CODE) {
            throw ApiException(
                respBody?.respCode,
                respBody?.respMessage ?: "Gagal memuat riwayat aktivitas.",
            )
        }

        // 🔹 data.history, BUKAN data langsung (data juga bawa "number")
        return respBody.data.history
    }
    override fun clear() = reset()

    private fun reset() {
        _items.clear()
        offset = 0
        _hasMore = true
    }
}