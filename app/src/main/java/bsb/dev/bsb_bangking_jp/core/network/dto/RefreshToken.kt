package bsb.dev.bsb_bangking_jp.core.network.dto

/** Body request untuk POST /auth/refresh. */
data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int = 30,
)

/** Response dari POST /auth/refresh -- accessToken & refreshToken baru. */
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
