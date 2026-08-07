package bsb.dev.bsb_bangking_jp.feature.login_existing.data

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("otp") val otp: String,
)