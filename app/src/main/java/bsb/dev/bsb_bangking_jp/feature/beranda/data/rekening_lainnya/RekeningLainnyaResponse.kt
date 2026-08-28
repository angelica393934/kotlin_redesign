package bsb.dev.bsb_bangking_jp.feature.beranda.data.rekening_lainnya

import com.google.gson.annotations.SerializedName

data class RekeningLainnyaResponse(
    @SerializedName("respCode") val respCode: String = "",
    @SerializedName("respMessage") val respMessage: String = "",
    @SerializedName("data") val data: List<RekeningItem> = emptyList(),
)

data class RekeningItem(
    @SerializedName("name") val name: String = "",
    @SerializedName("number") val number: String = "",
    @SerializedName("accounttypename") val accountTypeName: String = "",
    @SerializedName("visible") val visible: Boolean = false,
    @SerializedName("isprimary") val isPrimary: Boolean = false,
    @SerializedName("external") val external: RekeningExternal = RekeningExternal(),
)

data class RekeningExternal(
    @SerializedName("data") val data: RekeningExternalData = RekeningExternalData(),
    @SerializedName("responseCode") val responseCode: String = "",
    @SerializedName("responseMessage") val responseMessage: String = "",
)

data class RekeningExternalData(
    @SerializedName("accountInfos") val accountInfos: List<RekeningAccountInfo> = emptyList(),
    @SerializedName("accountNo") val accountNo: String = "",
    @SerializedName("additionalInfo") val additionalInfo: AdditionalInfo = AdditionalInfo(),
    @SerializedName("name") val name: String = "",
    @SerializedName("partnerReferenceNo") val partnerReferenceNo: String = "",
    @SerializedName("referenceNo") val referenceNo: String = "",
    @SerializedName("responseCode") val responseCode: String = "",
    @SerializedName("responseMessage") val responseMessage: String = "",
)

data class RekeningAccountInfo(
    @SerializedName("amount") val amount: RekeningBalance = RekeningBalance(),
    @SerializedName("availableBalance") val availableBalance: RekeningBalance = RekeningBalance(),
    @SerializedName("balanceType") val balanceType: String = "",
    @SerializedName("currentMultilateralLimit") val currentMultilateralLimit: RekeningBalance = RekeningBalance(),
    @SerializedName("floatAmount") val floatAmount: RekeningBalance = RekeningBalance(),
    @SerializedName("holdAmount") val holdAmount: RekeningBalance = RekeningBalance(),
    @SerializedName("ledgerBalance") val ledgerBalance: RekeningBalance = RekeningBalance(),
    @SerializedName("registrationStatusCode") val registrationStatusCode: String = "",
    @SerializedName("status") val status: String = "",
)

data class RekeningBalance(
    @SerializedName("currency") val currency: String = "",
    @SerializedName("value") val value: String = "0",
)

data class AdditionalInfo(
    @SerializedName("channelId") val channelId: String = "",
    @SerializedName("forwarderId") val forwarderId: String = "",
    @SerializedName("responseCode") val responseCode: String = "",
    @SerializedName("responseID") val responseId: String = "",
    @SerializedName("responseMessage") val responseMessage: String = "",
    @SerializedName("terminalId") val terminalId: String = "",
)

/** Padanan cashInfo lookup: firstWhere balanceType == "CASH", orElse ambil yang pertama. */
fun RekeningItem.cashInfo(): RekeningAccountInfo? {
    val accountInfos = external.data.accountInfos
    return accountInfos.firstOrNull { it.balanceType.uppercase() == "CASH" }
        ?: accountInfos.firstOrNull()
}

fun RekeningItem.cashBalanceValue(): Double =
    cashInfo()?.availableBalance?.value?.toDoubleOrNull() ?: 0.0