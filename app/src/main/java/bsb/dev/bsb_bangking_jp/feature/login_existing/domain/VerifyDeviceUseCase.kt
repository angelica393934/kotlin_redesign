// feature/login_existing/domain/VerifyDeviceUseCase.kt
package bsb.dev.bsb_bangking_jp.feature.login_existing.domain

class VerifyDeviceUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(challengeToken: String, phoneNumber: String): Result<Unit> =
        repository.verifyDevice(challengeToken, phoneNumber)
}