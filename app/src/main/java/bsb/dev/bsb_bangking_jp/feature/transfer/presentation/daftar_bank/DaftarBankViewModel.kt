// feature/transfer/presentation/DaftarBankViewModel.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.presentation.daftar_bank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.daftar_bank.DaftarBankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SESAMA_BANK_CODE = "120"

class DaftarBankViewModel(
    private val repository: DaftarBankRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DaftarBankUiState>(DaftarBankUiState.Initial)
    val uiState: StateFlow<DaftarBankUiState> = _uiState.asStateFlow()

    /** Padanan `_onGetDaftarBank` di Bloc -- kalau sudah Success, tidak fetch ulang. */
    fun getDaftarBank(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value is DaftarBankUiState.Success) return

        viewModelScope.launch {
            _uiState.update { DaftarBankUiState.Loading }
            try {
                val banks = repository.getDaftarBank(forceRefresh)

                if (banks.isEmpty()) {
                    _uiState.update { DaftarBankUiState.Error("Data bank kosong") }
                    return@launch
                }

                val sesamaBank = banks.filter { it.bankCode == SESAMA_BANK_CODE }
                val bankLain = banks
                    .filter { it.bankCode != SESAMA_BANK_CODE }
                    .sortedBy { it.bankName.lowercase() }

                _uiState.update { DaftarBankUiState.Success(sesamaBank, bankLain) }
            } catch (e: Exception) {
                val message = (e as? ApiException)?.respMessage
                    ?: e.message
                    ?: "Terjadi kesalahan, silakan coba lagi."
                _uiState.update { DaftarBankUiState.Error(message) }
            }
        }
    }

    fun retry() = getDaftarBank(forceRefresh = true)
}