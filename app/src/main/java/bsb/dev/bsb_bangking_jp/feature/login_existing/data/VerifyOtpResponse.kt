// feature/login_existing/data/VerifyOtpResponse.kt
package bsb.dev.bsb_bangking_jp.feature.login_existing.data

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: VerifyOtpData? = null,
)

data class VerifyOtpData(
    @SerializedName("challenge_token") val challengeToken: String? = null,
)