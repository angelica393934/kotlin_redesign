package bsb.dev.bsb_bangking_jp.feature.splash.data

import com.google.gson.annotations.SerializedName

data class InitDeviceRequest(
    @SerializedName("devicename") val deviceName: String,
    @SerializedName("os") val os: String,
    @SerializedName("public_key") val publicKey: String,
    @SerializedName("iccid") val iccid: String = "",
    @SerializedName("imei") val imei: String = "",
    @SerializedName("imsi") val imsi: String = "",
)