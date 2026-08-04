package bsb.dev.bsb_bangking_jp.domain.usecase

import bsb.dev.bsb_bangking_jp.core.network.dto.LoginResponse
import bsb.dev.bsb_bangking_jp.core.util.ApiResult
import bsb.dev.bsb_bangking_jp.data.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        expiresInMins: Int = 30,
    ): ApiResult<LoginResponse> {
        return repository.login(
            username = username,
            password = password,
            expiresInMins = expiresInMins,
        )
    }
}
