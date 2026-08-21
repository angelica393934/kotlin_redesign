// feature/transfer/domain/BankItem.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.domain.daftar_bank

data class BankItem(
    val bankCode: String,
    val bankName: String,
    val picture: String? = null,
)