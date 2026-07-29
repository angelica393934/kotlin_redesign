package bsb.dev.bsb_bangking_jp.core.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Response dari POST /auth/login (dummyjson.com).
 * Field disesuaikan dengan payload asli API tersebut.
 */
data class LoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
)
