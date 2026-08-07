package bsb.dev.bsb_bangking_jp.feature.init.domain

interface InitRepository {
    suspend fun initDevice(): Result<Unit>
}