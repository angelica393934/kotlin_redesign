package bsb.dev.bsb_bangking_jp.shared.account_source.data

import com.google.gson.annotations.SerializedName

data class SetPrimaryAccountRequest(
    @SerializedName("accountNumber") val accountNumber: String,
)

data class SetPrimaryAccountResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
)