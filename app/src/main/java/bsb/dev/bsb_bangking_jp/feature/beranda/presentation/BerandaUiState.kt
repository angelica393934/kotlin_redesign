// feature/beranda/presentation/BerandaUiState.kt
package bsb.dev.bsb_bangking_jp.feature.beranda.presentation

import bsb.dev.bsb_bangking_jp.shared.profile.data.ProfileData
import bsb.dev.bsb_bangking_jp.shared.account_source.data.RekeningItem
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.get_banner.BannerItem

data class BerandaUiState(
    val isProfileLoading: Boolean = false,
    val profile: ProfileData? = null,
    val profileError: String? = null,

    val isRekeningLoading: Boolean = false,
    val isRekeningRefreshing: Boolean = false,
    val rekeningList: List<RekeningItem>? = null,
    val rekeningError: String? = null,

    val isSettingPrimaryAccount: Boolean = false,

    val isBannerLoading: Boolean = false,
    val bannerList: List<BannerItem>? = null,
    val bannerError: String? = null,
)

sealed class BerandaUiEvent {
    data class ShowToastSuccess(val message: String) : BerandaUiEvent()
    data class ShowToastError(val message: String) : BerandaUiEvent()
}