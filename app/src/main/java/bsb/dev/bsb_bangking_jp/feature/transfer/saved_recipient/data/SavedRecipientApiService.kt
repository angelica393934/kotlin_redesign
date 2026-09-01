package bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.HeaderMap
import retrofit2.http.PUT
import retrofit2.http.Tag

interface SavedRecipientApiService {

    @GET("v1/dashboard/getsavedrecipients")
    suspend fun getSavedRecipients(
        @HeaderMap headers: Map<String, String>,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<SavedRecipientListResponse>

    @PUT("v1/dashboard/putsavedrecipients")
    suspend fun updateSavedRecipient(
        @HeaderMap headers: Map<String, String>,
        @Body body: UpdateSavedRecipientRequest,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<SavedRecipientActionResponse>

    // Retrofit tidak punya anotasi @DELETE dengan body, jadi pakai @HTTP manual
    // (hasBody = true) -- padanan Dio yang bisa DELETE + data langsung.
    @HTTP(method = "DELETE", path = "v1/dashboard/deletesavedrecipients", hasBody = true)
    suspend fun deleteSavedRecipient(
        @HeaderMap headers: Map<String, String>,
        @Body body: DeleteSavedRecipientRequest,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<SavedRecipientActionResponse>
}