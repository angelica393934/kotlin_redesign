package bsb.dev.bsb_bangking_jp.core.get_image.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.get_image.domain.ImageRepository
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Padanan GetImageRepository (Flutter). Sengaja TIDAK PERNAH throw -- gambar gagal
 * cukup dianggap "tidak ada", tidak boleh mengganggu layar yang menampilkannya.
 *
 * PENTING: kegagalan TIDAK di-cache permanen. Kalau geturlnews gagal ambil gambar
 * di Beranda, lalu user pindah ke getallnews/getnewsbyid yang butuh gambar sama,
 * repository ini akan MENCOBA LAGI -- bukan langsung dianggap kosong selamanya.
 * Yang di-cache permanen cuma hasil SUKSES (biar tidak hit API berulang untuk
 * gambar yang memang ada).
 */
class ImageRepositoryImpl(
    private val api: ImageApiService,
    private val secureStorage: SecureStorageService,
) : ImageRepository, ClearableRepository {

    private val successCache = mutableMapOf<String, ByteArray>()

    // 🔹 Coalescing: cegah beberapa consumer yang minta path SAMA di waktu BERSAMAAN
    // menembak API berkali-kali secara paralel. Bukan cache kegagalan -- begitu
    // request ini selesai (sukses/gagal), entry-nya langsung dibuang (lihat finally).
    private val inFlight = mutableMapOf<String, CompletableDeferred<ByteArray?>>()
    private val inFlightMutex = Mutex()

    override suspend fun getImage(path: String?): ByteArray? {
        val cleanPath = extractPath(path)
        if (cleanPath.isEmpty()) return null

        successCache[cleanPath]?.let { return it }

        // Kalau ada request lain yang sedang jalan utk path yang sama, ikut nunggu
        // hasilnya saja -- jangan ikut nembak API lagi.
        val existing = inFlightMutex.withLock { inFlight[cleanPath] }
        if (existing != null) return existing.await()

        val deferred = CompletableDeferred<ByteArray?>()
        inFlightMutex.withLock { inFlight[cleanPath] = deferred }

        try {
            val result = fetchFromApi(cleanPath)
            if (result != null) successCache[cleanPath] = result
            deferred.complete(result)
            return result
        } finally {
            inFlightMutex.withLock { inFlight.remove(cleanPath) }
        }
    }

    private suspend fun fetchFromApi(cleanPath: String): ByteArray? {
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

            if (!response.isSuccessful) return null

            val bytes = response.body()?.bytes()
            if (bytes == null || bytes.isEmpty()) null else bytes
        } catch (e: Exception) {
            null
        }
    }

    /** Padanan ImagePathUtils.extractImagePath -- buang base URL kalau path masih full URL. */
    private fun extractPath(fullPath: String?): String {
        if (fullPath.isNullOrBlank()) return ""
        return fullPath.substringAfter("://").substringAfter("/", missingDelimiterValue = fullPath)
    }

    override fun clear() {
        successCache.clear()
        // inFlight sengaja tidak disentuh -- request yang sedang berjalan biarkan selesai wajar,
        // deferred-nya akan clear diri sendiri lewat finally di atas.
    }
}