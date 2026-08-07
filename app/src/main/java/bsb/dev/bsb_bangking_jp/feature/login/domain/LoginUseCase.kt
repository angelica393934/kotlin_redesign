package bsb.dev.bsb_bangking_jp.feature.login.domain
class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(useridLogin: String, passcode: String): Result<Unit> =
        repository.login(useridLogin, passcode)
}