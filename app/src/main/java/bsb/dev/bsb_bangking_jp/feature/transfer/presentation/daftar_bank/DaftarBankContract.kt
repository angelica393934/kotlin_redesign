// feature/transfer/presentation/DaftarBankContract.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.presentation.daftar_bank

import bsb.dev.bsb_bangking_jp.feature.transfer.domain.daftar_bank.BankItem

sealed interface DaftarBankUiState {
    data object Initial : DaftarBankUiState
    data object Loading : DaftarBankUiState
    data class Success(
        val sesamaBank: List<BankItem>,
        val bankLain: List<BankItem>,
    ) : DaftarBankUiState
    data class Error(val message: String) : DaftarBankUiState
}