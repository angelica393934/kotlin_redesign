package bsb.dev.bsb_bangking_jp.feature.login.data

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("useridlogin") val useridLogin: String,
    @SerializedName("passcode") val passcode: String,
)