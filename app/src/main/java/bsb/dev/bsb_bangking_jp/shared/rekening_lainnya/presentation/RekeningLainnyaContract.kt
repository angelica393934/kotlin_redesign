package bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.presentation

import bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.data.RekeningItem

data class RekeningLainnyaUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val rekeningList: List<RekeningItem>? = null,
    val error: String? = null,
    val isSettingPrimaryAccount: Boolean = false,
)

sealed class RekeningLainnyaUiEvent {
    data class ShowToastSuccess(val message: String) : RekeningLainnyaUiEvent()
    data class ShowToastError(val message: String) : RekeningLainnyaUiEvent()
}