package bsb.dev.bsb_bangking_jp.core.get_image.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.get_image.domain.ImageRepository
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository

/**
 * Padanan GetImageRepository (Flutter). Sengaja TIDAK PERNAH throw -- gambar gagal
 * cukup dianggap "tidak ada", tidak boleh mengganggu layar yang menampilkannya.
 */
class ImageRepositoryImpl(
    private val api: ImageApiService,
    private val secureStorage: SecureStorageService,
) : ImageRepository, ClearableRepository {

    private val cache = mutableMapOf<String, ByteArray?>()
    private val emptyPaths = mutableSetOf<String>()

    override suspend fun getImage(path: String?): ByteArray? {
        val cleanPath = extractPath(path)
        if (cleanPath.isEmpty()) return null

        cache[cleanPath]?.let { return it }
        if (cleanPath in emptyPaths) return null

        return try {
            val privateKey = secureStorage.getPrivateKey() ?: return null
            val timestamp = ApiHeaders.currentTimestamp()
            val signature = SignatureUtils.sign(emptyMap<String, String>(), timestamp, privateKey)
            val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

            val response = api.getImage(
                headers = headers,
                path = cleanPath,
                tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
            )

            if (!response.isSuccessful) {
                emptyPaths += cleanPath
                return null
            }

            val bytes = response.body()?.bytes()
            if (bytes == null || bytes.isEmpty()) {
                emptyPaths += cleanPath
                return null
            }

            cache[cleanPath] = bytes
            bytes
        } catch (e: Exception) {
            emptyPaths += cleanPath
            null
        }
    }

    /** Padanan ImagePathUtils.extractImagePath -- buang base URL kalau path masih full URL. */
    private fun extractPath(fullPath: String?): String {
        if (fullPath.isNullOrBlank()) return ""
        return fullPath.substringAfter("://").substringAfter("/", missingDelimiterValue = fullPath)
    }

    override fun clear() {
        cache.clear()
        emptyPaths.clear()
    }
}