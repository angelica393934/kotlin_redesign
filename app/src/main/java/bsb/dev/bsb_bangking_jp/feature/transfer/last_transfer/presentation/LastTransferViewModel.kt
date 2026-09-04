package bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.domain.LastTransferRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LastTransferViewModel(
    private val repository: LastTransferRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LastTransferUiState>(LastTransferUiState.Initial)
    val uiState: StateFlow<LastTransferUiState> = _uiState.asStateFlow()

    /** Padanan `_onLoad` di Bloc  -- kalau sudah Success, tidak fetch ulang kecuali forceRefresh. */
    fun load(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value is LastTransferUiState.Success) return

        viewModelScope.launch {
            _uiState.update { LastTransferUiState.Loading }
            try {
                val items = repository.getLastTransfer(forceRefresh)
                _uiState.update { LastTransferUiState.Success(items) }
            } catch (e: Exception) {
                val message = (e as? ApiException)?.respMessage
                    ?: e.message
                    ?: "Terjadi kesalahan, silakan coba lagi."
                _uiState.update { LastTransferUiState.Error(message) }
            }
        }
    }

    fun retry() = load(forceRefresh = true)
}