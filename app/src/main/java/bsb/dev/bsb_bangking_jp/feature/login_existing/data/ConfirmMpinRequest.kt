package bsb.dev.bsb_bangking_jp.feature.login_existing.data

import com.google.gson.annotations.SerializedName

// Catatan: field body ini "mobilenumber" (BUKAN "identifier"), sesuai contoh backend-mu.
data class ConfirmMpinRequest(
    @SerializedName("mobilenumber") val mobileNumber: String,
    @SerializedName("confirm_mpin") val confirmMpin: String,
)

data class ConfirmMpinResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
)