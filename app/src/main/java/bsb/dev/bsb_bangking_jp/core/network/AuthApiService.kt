package bsb.dev.bsb_bangking_jp.core.network

import bsb.dev.bsb_bangking_jp.core.network.dto.LoginRequest
import bsb.dev.bsb_bangking_jp.core.network.dto.LoginResponse
import bsb.dev.bsb_bangking_jp.core.network.dto.MeResponse
import bsb.dev.bsb_bangking_jp.core.network.dto.RefreshRequest
import bsb.dev.bsb_bangking_jp.core.network.dto.RefreshResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    @GET("auth/me")
    suspend fun getMe(@Header("Authorization") authorizationHeader: String): MeResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshRequest): RefreshResponse
}
