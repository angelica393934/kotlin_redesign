package bsb.dev.bsb_bangking_jp.shared.get_image.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.shared.get_image.domain.ImageCategory
import bsb.dev.bsb_bangking_jp.shared.get_image.domain.ImageRepository
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ImageRepositoryImpl(
    private val api: ImageApiService,
    private val secureStorage: SecureStorageService,
) : ImageRepository, ClearableRepository {

    private val successCache = mutableMapOf<String, ByteArray>()
    private val inFlight = mutableMapOf<String, CompletableDeferred<ByteArray?>>()
    private val inFlightMutex = Mutex()

    override suspend fun getImage(path: String?, category: ImageCategory): ByteArray? {
        val fileName = extractFileName(path)
        if (fileName.isEmpty()) return null

        // 🔹 Path final yang benar-benar dikirim ke API, mis. "banner/1770604454_BI Fast.jpeg"
        val requestPath = "${category.segment}/$fileName"

        successCache[requestPath]?.let { return it }

        val existing = inFlightMutex.withLock { inFlight[requestPath] }
        if (existing != null) return existing.await()

        val deferred = CompletableDeferred<ByteArray?>()
        inFlightMutex.withLock { inFlight[requestPath] = deferred }

        try {
            val result = fetchFromApi(requestPath)
            if (result != null) successCache[requestPath] = result
            deferred.complete(result)
            return result
        } finally {
            inFlightMutex.withLock { inFlight.remove(requestPath) }
        }
    }

    private suspend fun fetchFromApi(requestPath: String): ByteArray? {
        return try {
            val privateKey = secureStorage.getPrivateKey() ?: return null
            val timestamp = ApiHeaders.currentTimestamp()
            val signature = SignatureUtils.sign(emptyMap<String, String>(), timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.getImage(
                headers = headers,
                path = requestPath,
                tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
            )

            if (!response.isSuccessful) return null

            val bytes = response.body()?.bytes()
            if (bytes == null || bytes.isEmpty()) null else bytes
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Ambil NAMA FILE saja dari path mentah backend, tidak peduli prefix-nya apa
     * ("/v1/image/xxx.jpeg", "v1/somethingelse/xxx.jpeg", full URL, dll) -- prefix
     * lama ini SELALU dibuang, digantikan `category.segment` di [getImage].
     */
    private fun extractFileName(fullPath: String?): String {
        if (fullPath.isNullOrBlank()) return ""
        val withoutScheme = if (fullPath.contains("://")) {
            fullPath.substringAfter("://").substringAfter("/", missingDelimiterValue = "")
        } else {
            fullPath
        }
        return withoutScheme.substringAfterLast("/")
    }

    override fun clear() {
        successCache.clear()
    }
}