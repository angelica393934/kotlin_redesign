// feature/beranda/data/ProfileResponse.kt
package bsb.dev.bsb_bangking_jp.feature.beranda.data.profile

import bsb.dev.bsb_bangking_jp.feature.beranda.data.rekening_lainnya.AdditionalInfo
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("respCode") val respCode: String = "",
    @SerializedName("respMessage") val respMessage: String = "",
    @SerializedName("data") val data: ProfileData = ProfileData(),
)

data class ProfileData(
    @SerializedName("external") val external: ProfileExternal = ProfileExternal(),
    @SerializedName("accounts") val accounts: Any? = null, // API selalu kirim null
    @SerializedName("user") val user: UserData = UserData(),
)

data class ProfileExternal(
    @SerializedName("data") val data: ProfileExternalData = ProfileExternalData(),
    @SerializedName("responseCode") val responseCode: String = "",
    @SerializedName("responseMessage") val responseMessage: String = "",
)

data class ProfileExternalData(
    @SerializedName("accountInfos") val accountInfos: List<ProfileAccountInfo> = emptyList(),
    @SerializedName("accountNo") val accountNo: String = "",
    @SerializedName("additionalInfo") val additionalInfo: AdditionalInfo = AdditionalInfo(),
    @SerializedName("name") val name: String = "",
    @SerializedName("partnerReferenceNo") val partnerReferenceNo: String = "",
    @SerializedName("referenceNo") val referenceNo: String = "",
    @SerializedName("responseCode") val responseCode: String = "",
    @SerializedName("responseMessage") val responseMessage: String = "",
)

data class ProfileAccountInfo(
    @SerializedName("balanceType") val balanceType: String = "",
    @SerializedName("amount") val amount: ProfileAmount = ProfileAmount(),
    @SerializedName("availableBalance") val availableBalance: ProfileAmount = ProfileAmount(),
    @SerializedName("ledgerBalance") val ledgerBalance: ProfileAmount = ProfileAmount(),
    @SerializedName("holdAmount") val holdAmount: ProfileAmount = ProfileAmount(),
    @SerializedName("currentMultilateralLimit") val currentMultilateralLimit: ProfileAmount = ProfileAmount(),
    @SerializedName("floatAmount") val floatAmount: ProfileAmount = ProfileAmount(),
    @SerializedName("registrationStatusCode") val registrationStatusCode: String = "",
    @SerializedName("status") val status: String = "",
)

data class ProfileAmount(
    @SerializedName("currency") val currency: String = "",
    @SerializedName("value") val value: Double = 0.0,
)

data class UserData(
    @SerializedName("mobilenumber") val mobileNumber: String = "",
    @SerializedName("customername") val customerName: String = "",
    @SerializedName("photoprofile") val photoProfile: String = "",
    @SerializedName("atmcardno") val atmCardNo: String = "",
    @SerializedName("maskphone") val maskPhone: String = "-",
    @SerializedName("userid") val userId: String = "",
)