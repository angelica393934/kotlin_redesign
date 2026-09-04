package bsb.dev.bsb_bangking_jp.shared.logout.data

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
)