package bsb.dev.bsb_bangking_jp.feature.init.data

import com.google.gson.annotations.SerializedName

// TODO: sesuaikan field dengan response asli backend, ini masih placeholder
data class InitDeviceResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
)