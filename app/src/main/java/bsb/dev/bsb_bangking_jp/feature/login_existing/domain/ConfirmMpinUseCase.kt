// feature/login_existing/domain/ConfirmMpinUseCase.kt
package bsb.dev.bsb_bangking_jp.feature.login_existing.domain

class ConfirmMpinUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(phoneNumber: String, confirmMpin: String): Result<Unit> =
        repository.confirmMpin(phoneNumber, confirmMpin)
}