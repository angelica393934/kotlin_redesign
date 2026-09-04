package bsb.dev.bsb_bangking_jp.shared.logout.domain

class LogoutUseCase(private val repository: LogoutRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.logout()
}
