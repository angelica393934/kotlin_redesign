package bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.presentation

import bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.domain.LastTransferItem

sealed interface LastTransferUiState {
    data object Initial : LastTransferUiState
    data object Loading : LastTransferUiState
    data class Success(val items: List<LastTransferItem>) : LastTransferUiState
    data class Error(val message: String) : LastTransferUiState
}