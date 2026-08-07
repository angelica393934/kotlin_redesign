// feature/login_existing/domain/ResendOtpUseCase.kt
package bsb.dev.bsb_bangking_jp.feature.login_existing.domain

class ResendOtpUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(identifier: String): Result<Unit> = repository.resendOtp(identifier)
}