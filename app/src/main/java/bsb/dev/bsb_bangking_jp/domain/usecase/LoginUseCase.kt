package bsb.dev.bsb_bangking_jp.domain.usecase

import bsb.dev.bsb_bangking_jp.core.network.dto.LoginResponse
import bsb.dev.bsb_bangking_jp.core.util.ApiResult
import bsb.dev.bsb_bangking_jp.data.repository.AuthRepository

/**
 * Use case (Command pattern) untuk 1 aksi bisnis spesifik: login.
 *
 * Kenapa perlu layer ini padahal cuma manggil Repository?
 * - Selaras dengan "Clean Architecture (MVVM)" & pattern "Command" di tech stack.
 * - ViewModel jadi tidak tahu detail Repository, cuma tahu "jalankan LoginUseCase".
 * - Tempat aman untuk nambah validasi bisnis (bukan validasi UI) di kemudian
 *   hari, tanpa mengotori ViewModel maupun Repository.
 *
 * Dipanggil seperti fungsi biasa lewat operator invoke():
 *   loginUseCase(username, password)
 */
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
