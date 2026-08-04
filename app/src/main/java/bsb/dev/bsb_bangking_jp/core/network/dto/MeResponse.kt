package bsb.dev.bsb_bangking_jp.core.network.dto

/**
 * Response dari GET /auth/me. Bentuknya sama dengan LoginResponse tapi TANPA
 * accessToken/refreshToken -- endpoint ini cuma mengembalikan data profil.
 */
data class MeResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
)
