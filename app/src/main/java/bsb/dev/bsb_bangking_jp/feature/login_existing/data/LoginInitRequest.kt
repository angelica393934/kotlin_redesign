package bsb.dev.bsb_bangking_jp.feature.login_existing.data

import com.google.gson.annotations.SerializedName

data class LoginInitRequest(
    @SerializedName("identifier") val identifier: String,
)