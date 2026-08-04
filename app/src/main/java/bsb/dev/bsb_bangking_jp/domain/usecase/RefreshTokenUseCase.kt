package bsb.dev.bsb_bangking_jp.domain.usecase

import bsb.dev.bsb_bangking_jp.core.network.dto.RefreshResponse
import bsb.dev.bsb_bangking_jp.core.util.ApiResult
import bsb.dev.bsb_bangking_jp.data.repository.AuthRepository

/**
 * Use case untuk tukar refreshToken lama dengan pasangan token baru
 * (POST /auth/refresh).
 *
 * CATATAN: prosesnya sudah lengkap sampai ke BerandaViewModel.refreshAccessToken(),
 * tapi SENGAJA belum ada tombol/trigger UI apapun yang memanggilnya. Panggil
 * fungsi itu nanti begitu ada kebutuhan nyata, mis.:
 * - Tombol manual "Perbarui sesi" di halaman pengaturan, atau
 * - Otomatis dipanggil saat request lain balikin 401 (access token expired).
 */
class RefreshTokenUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        refreshToken: String,
        expiresInMins: Int = 30,
    ): ApiResult<RefreshResponse> {
        return repository.refreshToken(refreshToken, expiresInMins)
    }
}
