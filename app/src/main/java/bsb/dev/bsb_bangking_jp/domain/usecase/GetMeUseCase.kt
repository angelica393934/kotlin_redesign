package bsb.dev.bsb_bangking_jp.domain.usecase

import bsb.dev.bsb_bangking_jp.core.network.dto.MeResponse
import bsb.dev.bsb_bangking_jp.core.util.ApiResult
import bsb.dev.bsb_bangking_jp.data.repository.AuthRepository

/**
 * Use case untuk ambil ulang data profil terkini (GET /auth/me).
 * Dipakai saat pull-to-refresh di BerandaPage.
 */
class GetMeUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(accessToken: String): ApiResult<MeResponse> {
        return repository.getMe(accessToken)
    }
}
