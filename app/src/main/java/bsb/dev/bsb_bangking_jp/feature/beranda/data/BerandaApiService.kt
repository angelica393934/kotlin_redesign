// feature/beranda/data/BerandaApiService.kt
package bsb.dev.bsb_bangking_jp.feature.beranda.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import bsb.dev.bsb_bangking_jp.feature.beranda.data.get_banner.GetBannerResponse
import bsb.dev.bsb_bangking_jp.shared.profile.data.ProfileResponse
import bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.data.RekeningLainnyaResponse
import bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.data.SetPrimaryAccountRequest
import bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.data.SetPrimaryAccountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.PUT
import retrofit2.http.Tag

interface BerandaApiService {

    @GET("v1/info/getbanner")
    suspend fun getBanner(
        @HeaderMap headers: Map<String, String>,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<GetBannerResponse>
}