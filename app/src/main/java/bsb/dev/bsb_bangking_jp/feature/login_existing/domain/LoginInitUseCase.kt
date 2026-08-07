package bsb.dev.bsb_bangking_jp.feature.login_existing.domain

class LoginInitUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(identifier: String): Result<Unit> = repository.loginInit(identifier)
}