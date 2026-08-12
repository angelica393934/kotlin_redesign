// feature/beranda/data/ProfileRepositoryImpl.kt
package bsb.dev.bsb_bangking_jp.feature.beranda.data

import bsb.dev.bsb_bangking_jp.core.crypto.SignatureUtils
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhase
import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.util.retry
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.ProfileRepository

private const val SUCCESS_CODE = "0000"

class ProfileRepositoryImpl(
    private val api: BerandaApiService,
    private val secureStorage: SecureStorageService,
) : ProfileRepository, ClearableRepository {

    private var cache: ProfileData? = null // 🔹 ProfileData, BUKAN ProfileExternalData

    override val hasProfile: Boolean get() = cache != null
    override val cachedProfile: ProfileData? get() = cache // 🔹 ProfileData

    override suspend fun getProfile(forceRefresh: Boolean): ProfileData { // 🔹 ProfileData
        if (!forceRefresh && cache != null) {
            return cache!!
        }
        val fresh = retry(maxAttempt = 3) { fetchProfile() }
        cache = fresh
        return fresh
    }

    private suspend fun fetchProfile(): ProfileData { // 🔹 ProfileData
        val privateKey = secureStorage.getPrivateKey()
            ?: throw IllegalStateException("Private key tidak ditemukan, device belum ter-init.")

        val timestamp = ApiHeaders.currentTimestamp()
        val signature = SignatureUtils.sign(emptyMap<String, String>(), timestamp, privateKey)
        val headers = ApiHeaders.withSignature(signature, ApiHeaders.full(timestamp))

        val response = api.getProfile(
            headers = headers,
            tokenPhase = TokenPhaseTag(TokenPhase.LOGIN),
        )

        if (!response.isSuccessful) {
            val message = response.errorBody()?.string()
            throw ApiException(null, message ?: "Terjadi kendala saat menampilkan profil Anda.")
        }

        val body = response.body()
        if (body?.respCode != SUCCESS_CODE) {
            throw ApiException(
                body?.respCode,
                body?.respMessage ?: "Terjadi kendala saat menampilkan profil Anda."
            )
        }

        return body.data // 🔹 langsung return ProfileData penuh, BUKAN body.data.external.data
    }

    override fun clear() {
        cache = null
    }
}