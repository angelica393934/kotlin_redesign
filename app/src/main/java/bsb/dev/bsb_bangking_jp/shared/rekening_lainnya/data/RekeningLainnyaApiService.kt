package bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.PUT
import retrofit2.http.Tag

interface RekeningLainnyaApiService {

    @GET("v1/dashboard/getaccountsourceprofile")
    suspend fun getAccountSourceProfile(
        @HeaderMap headers: Map<String, String>,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<RekeningLainnyaResponse>

    @PUT("v1/dashboard/setprimaryaccount")
    suspend fun setPrimaryAccount(
        @HeaderMap headers: Map<String, String>,
        @Body body: SetPrimaryAccountRequest,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<SetPrimaryAccountResponse>
}