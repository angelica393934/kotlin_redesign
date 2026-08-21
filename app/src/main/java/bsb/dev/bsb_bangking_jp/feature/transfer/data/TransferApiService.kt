package bsb.dev.bsb_bangking_jp.feature.transfer.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Tag

interface TransferApiService {

    @POST("v1/dashboard/getaccountdest")
    suspend fun getAccountDest(
        @HeaderMap headers: Map<String, String>,
        @Body body: GetAccountDestRequest,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<GetAccountDestResponse>

    @POST("v1/dashboard/savedrecipients")
    suspend fun saveRecipient(
        @HeaderMap headers: Map<String, String>,
        @Body body: SaveRecipientRequest,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<SaveRecipientResponse>

    @POST("v1/dashboard/transfer")
    suspend fun transfer(
        @HeaderMap headers: Map<String, String>,
        @Body body: TransferApiRequest,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<TransferApiResponse>

    @POST("v1/transfer/confirmtransfer")
    suspend fun confirmTransfer(
        @HeaderMap headers: Map<String, String>,
        @Body body: ConfirmTransferRequest,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<ConfirmTransferApiResponse>

    @GET("v1/gettransferpurpose")
    suspend fun getTransferPurpose(
        @HeaderMap headers: Map<String, String>,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<TransferPurposeResponse>
}