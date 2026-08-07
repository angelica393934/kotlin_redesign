package bsb.dev.bsb_bangking_jp.feature.init.domain

class InitDeviceUseCase(private val repository: InitRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.initDevice()
}