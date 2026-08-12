// feature/beranda/presentation/BerandaUiState.kt
package bsb.dev.bsb_bangking_jp.feature.beranda.presentation

import bsb.dev.bsb_bangking_jp.feature.beranda.data.ProfileData
import bsb.dev.bsb_bangking_jp.feature.beranda.data.RekeningItem

data class BerandaUiState(
    val isProfileLoading: Boolean = false,
    val profile: ProfileData? = null,
    val profileError: String? = null,

    val isRekeningLoading: Boolean = false,
    val isRekeningRefreshing: Boolean = false,
    val rekeningList: List<RekeningItem>? = null,
    val rekeningError: String? = null,

    val isSettingPrimaryAccount: Boolean = false,
)

sealed class BerandaUiEvent {
    data class ShowToastSuccess(val message: String) : BerandaUiEvent()
    data class ShowToastError(val message: String) : BerandaUiEvent()
}