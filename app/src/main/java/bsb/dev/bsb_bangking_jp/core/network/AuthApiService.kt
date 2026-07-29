package bsb.dev.bsb_bangking_jp.core.network

import bsb.dev.bsb_bangking_jp.core.network.dto.LoginRequest
import bsb.dev.bsb_bangking_jp.core.network.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
