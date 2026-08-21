// feature/transfer/data/DaftarBankModels.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.data.daftar_bank

import bsb.dev.bsb_bangking_jp.feature.transfer.domain.daftar_bank.BankItem
import com.google.gson.annotations.SerializedName

data class DaftarBankResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: List<BankItemResponse> = emptyList(),
)

data class BankItemResponse(
    @SerializedName("code") val code: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("picture") val picture: String? = null,
)

/** Mapper response -> domain model. */
fun BankItemResponse.toDomain(): BankItem = BankItem(
    bankCode = code,
    bankName = name,
    picture = picture,
)