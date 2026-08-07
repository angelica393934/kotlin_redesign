package bsb.dev.bsb_bangking_jp.feature.login.domain

interface LoginRepository {
    suspend fun login(useridLogin: String, passcode: String): Result<Unit>
}