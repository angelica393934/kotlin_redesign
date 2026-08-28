package bsb.dev.bsb_bangking_jp.feature.beranda.presentation

import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.session.SessionClearer
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.profile.ProfileRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.RekeningLainnyaRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.get_banner.GetBannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BerandaViewModel(
    private val profileRepository: ProfileRepository,
    private val rekeningRepository: RekeningLainnyaRepository,
    private val sessionClearer: SessionClearer,
    private val bannerRepository: GetBannerRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _uiState = MutableStateFlow(BerandaUiState())
    val uiState: StateFlow<BerandaUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<BerandaUiEvent>(replay = 0)
    val uiEvent: SharedFlow<BerandaUiEvent> = _uiEvent.asSharedFlow()

    fun loadProfile(forceRefresh: Boolean = false) {
        scope.launch {
            if (!forceRefresh && profileRepository.hasProfile) {
                _uiState.update {
                    it.copy(isProfileLoading = false, profile = profileRepository.cachedProfile, profileError = null)
                }
                return@launch
            }
            _uiState.update { it.copy(isProfileLoading = true, profileError = null) }
            try {
                val result = profileRepository.getProfile(forceRefresh)
                _uiState.update { it.copy(isProfileLoading = false, profile = result, profileError = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProfileLoading = false,
                        profileError = (e as? ApiException)?.respMessage ?: e.message ?: "Terjadi kendala saat menampilkan profil Anda.",
                    )
                }
            }
        }
    }

    fun loadRekeningLainnya(forceRefresh: Boolean = false) {
        scope.launch {
            val hasExisting = _uiState.value.rekeningList != null

            if (hasExisting && forceRefresh) {
                _uiState.update { it.copy(isRekeningRefreshing = true, rekeningError = null) }
            } else {
                _uiState.update { it.copy(isRekeningLoading = true, rekeningError = null) }
            }

            try {
                val result = rekeningRepository.getRekeningLainnya(forceRefresh)
                _uiState.update {
                    it.copy(isRekeningLoading = false, isRekeningRefreshing = false, rekeningList = result, rekeningError = null)
                }
            } catch (e: Exception) {
                if (hasExisting) {
                    _uiState.update { it.copy(isRekeningLoading = false, isRekeningRefreshing = false) }
                } else {
                    _uiState.update {
                        it.copy(
                            isRekeningLoading = false,
                            isRekeningRefreshing = false,
                            rekeningError = (e as? ApiException)?.respMessage ?: e.message ?: "Gagal memuat data Rekening.",
                        )
                    }
                }
            }
        }
    }

    /** 🔹 Padanan `GetBannerBloc._onFetchBanner`. Tidak fetch ulang kalau sudah pernah sukses, kecuali forceRefresh. */
    fun loadBanner(forceRefresh: Boolean = false) {
        scope.launch {
            if (!forceRefresh && bannerRepository.hasData) {
                _uiState.update {
                    it.copy(isBannerLoading = false, bannerList = bannerRepository.cachedBanners, bannerError = null)
                }
                return@launch
            }

            _uiState.update { it.copy(isBannerLoading = true, bannerError = null) }
            try {
                val result = bannerRepository.getBanner(forceRefresh)
                _uiState.update { it.copy(isBannerLoading = false, bannerList = result, bannerError = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isBannerLoading = false,
                        bannerError = (e as? ApiException)?.respMessage ?: e.message ?: "Gagal memuat banner",
                    )
                }
            }
        }
    }


    // 🔹 INI YANG HILANG -- pastikan method ini ada di file-mu
    fun setPrimaryAccount(accountNumber: String) {
        scope.launch {
            _uiState.update { it.copy(isSettingPrimaryAccount = true) }

            rekeningRepository.setPrimaryAccount(accountNumber)
                .onSuccess {
                    _uiState.update { it.copy(isSettingPrimaryAccount = false) }
                    _uiEvent.emit(BerandaUiEvent.ShowToastSuccess("Rekening Utama berhasil dirubah"))
                    loadRekeningLainnya(forceRefresh = true)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isSettingPrimaryAccount = false) }
                    val message = (error as? ApiException)?.respMessage ?: error.message ?: "Gagal merubah rekening utama."
                    _uiEvent.emit(BerandaUiEvent.ShowToastError("Gagal merubah rekening utama. $message"))
                }
        }
    }

    fun refreshAll() {
        loadProfile(forceRefresh = true)
        loadRekeningLainnya(forceRefresh = true)
        loadBanner(forceRefresh = true)
    }

    fun logout() {
        sessionClearer.clearAll()
        _uiState.update { BerandaUiState() }
    }
}