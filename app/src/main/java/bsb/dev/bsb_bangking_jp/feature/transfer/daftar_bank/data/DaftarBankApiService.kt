// feature/transfer/data/DaftarBankApiService.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.daftar_bank.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Tag

interface DaftarBankApiService {
    @GET("v1/dashboard/getcodebank")
    suspend fun getDaftarBank(
        @HeaderMap headers: Map<String, String>,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<DaftarBankResponse>
}