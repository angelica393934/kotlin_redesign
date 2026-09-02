package bsb.dev.bsb_bangking_jp.shared.profile.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Tag

interface ProfileApiService {

    @GET("v1/dashboard/getprofile1")
    suspend fun getProfile(
        @HeaderMap headers: Map<String, String>,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<ProfileResponse>
}