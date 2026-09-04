package bsb.dev.bsb_bangking_jp.shared.logout.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Tag

interface LogoutApiService {
    // TODO: konfirmasi ke backend --  kirim `data: {}` (body kosong) di GET ini.
    // Diasumsikan itu cuma formalitas Dio, BUKAN kontrak GET-with-body sungguhan
    // (beda dengan gethistory/getlasttransfer1), jadi dipakai @GET biasa tanpa @Body.
    // Kalau ternyata backend menolak GET tanpa body, ganti ke GetWithBodyApiHelper.
    @GET("v1/dashboard/logout")
    suspend fun logout(
        @HeaderMap headers: Map<String, String>,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<LogoutResponse>
}