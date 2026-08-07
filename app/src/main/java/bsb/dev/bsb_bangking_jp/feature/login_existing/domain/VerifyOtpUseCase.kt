// feature/login_existing/domain/VerifyOtpUseCase.kt
package bsb.dev.bsb_bangking_jp.feature.login_existing.domain

class VerifyOtpUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(identifier: String, otp: String): Result<String> =
        repository.verifyOtp(identifier, otp)
}