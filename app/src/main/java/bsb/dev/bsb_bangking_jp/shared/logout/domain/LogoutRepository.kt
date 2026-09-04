package bsb.dev.bsb_bangking_jp.shared.logout.domain

interface LogoutRepository {
    suspend fun logout(): Result<Unit>
}