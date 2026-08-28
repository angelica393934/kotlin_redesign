package bsb.dev.bsb_bangking_jp.feature.beranda.data.rekening_lainnya

import com.google.gson.annotations.SerializedName

data class SetPrimaryAccountRequest(
    @SerializedName("accountNumber") val accountNumber: String,
)

data class SetPrimaryAccountResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
)