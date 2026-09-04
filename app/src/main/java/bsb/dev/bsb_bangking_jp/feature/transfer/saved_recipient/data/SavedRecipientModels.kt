// feature/transfer/data/saved_recipient/SavedRecipientModels.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.data

import bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.domain.SavedRecipientItem
import com.google.gson.annotations.SerializedName

data class SavedRecipientListResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: List<SavedRecipientDto> = emptyList(),
)

// Field mengikuti persis SavedRecipientModel.fromJson() di :
// "number" -> accountNumber, "name_code" -> bankName, "bank_code" -> bankCode.
data class SavedRecipientDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("number") val number: String = "",
    @SerializedName("alias") val alias: String = "",
    @SerializedName("name_code") val nameCode: String = "",
    @SerializedName("accountName") val accountName: String? = null,
    @SerializedName("bank_code") val bankCode: String = "",
)

data class UpdateSavedRecipientRequest(
    @SerializedName("id") val id: String,
    @SerializedName("alias") val alias: String,
)

data class DeleteSavedRecipientRequest(
    // selalu array, sama seperti versi Dart (`"id": ids`)
    @SerializedName("id") val id: List<String>,
)

data class SavedRecipientActionResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
)

fun SavedRecipientDto.toDomain(): SavedRecipientItem = SavedRecipientItem(
    id = id,
    accountNumber = number,
    alias = alias,
    bankName = nameCode,
    accountName = accountName,
    bankCode = bankCode,
)