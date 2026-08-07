// feature/login_existing/data/VerifyDeviceRequest.kt
package bsb.dev.bsb_bangking_jp.feature.login_existing.data

import com.google.gson.annotations.SerializedName

data class VerifyDeviceRequest(@SerializedName("identifier") val identifier: String)

data class VerifyDeviceResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: VerifyDeviceData? = null,
)

data class VerifyDeviceData(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
)