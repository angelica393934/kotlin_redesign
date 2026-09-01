package bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.data

import bsb.dev.bsb_bangking_jp.core.network.BaseRespCodeResponse
import bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.domain.LastTransferItem
import com.google.gson.annotations.SerializedName

data class GetLastTransferRequest(
    @SerializedName("accountNumber") val accountNumber: String,
)

data class LastTransferResponse(
    @SerializedName("respCode") override val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: List<LastTransferItemDto> = emptyList(),
) : BaseRespCodeResponse

data class LastTransferItemDto(
    @SerializedName("accountdestination") val accountDestination: String = "",
    @SerializedName("accountdestinationname") val accountDestinationName: String = "",
    @SerializedName("bank_name") val bankName: String = "",
    @SerializedName("codebank") val bankCode: String = "",
)

fun LastTransferItemDto.toDomain(): LastTransferItem = LastTransferItem(
    accountDestination = accountDestination,
    accountDestinationName = accountDestinationName,
    bankName = bankName,
    bankCode = bankCode,
)