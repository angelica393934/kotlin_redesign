package bsb.dev.bsb_bangking_jp.feature.login.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface LoginApiService {
    @POST("v1/login")
    suspend fun login(
        @HeaderMap headers: Map<String, String>,
        @Body body: LoginRequest,
    ): Response<LoginResponse>
}