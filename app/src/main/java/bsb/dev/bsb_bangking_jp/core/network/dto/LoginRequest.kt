package bsb.dev.bsb_bangking_jp.core.network.dto

/**
 * Body request untuk endpoint POST /auth/login (dummyjson.com).
 */
data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 30,
)
