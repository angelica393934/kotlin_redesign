package bsb.dev.bsb_bangking_jp.feature.aktivitas.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.HeaderMap
import retrofit2.http.Tag

interface ActivityHistoryApiService {
    // 🔹 Backend pakai GET dengan body (padanan Dio `_dio.get(..., data: body)`).
    // Retrofit tidak izinkan @Body di @GET biasa, jadi pakai @HTTP(hasBody = true).
    @HTTP(method = "GET", path = "v1/dashboard/gethistory", hasBody = true)
    suspend fun getHistory(
        @HeaderMap headers: Map<String, String>,
        @Body body: GetHistoryRequest,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<ActivityHistoryResponse>
}