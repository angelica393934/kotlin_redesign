package bsb.dev.bsb_bangking_jp.feature.init.data

import com.google.gson.annotations.SerializedName

data class InitDeviceRequest(
    @SerializedName("devicename") val deviceName: String,
    @SerializedName("os") val os: String,
    @SerializedName("public_key") val publicKey: String,
    // Dikosongkan dulu sesuai kesepakatan -- IMEI/IMSI/ICCID tidak bisa diakses
    // app biasa di Android 10+ tanpa privileged permission (READ_PRIVILEGED_PHONE_STATE).
    @SerializedName("iccid") val iccid: String = "",
    @SerializedName("imei") val imei: String = "",
    @SerializedName("imsi") val imsi: String = "",
)