package bsb.dev.bsb_bangking_jp.feature.beranda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.session.SessionManager
import bsb.dev.bsb_bangking_jp.core.session.UserProfile
import bsb.dev.bsb_bangking_jp.core.util.ApiResult
import bsb.dev.bsb_bangking_jp.domain.usecase.GetMeUseCase
import bsb.dev.bsb_bangking_jp.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BerandaViewModel(
    private val getMeUseCase: GetMeUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
) : ViewModel() {

    private val _profile = MutableStateFlow(SessionManager.getUserProfile())
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun refresh() {
        val token = SessionManager.getAccessToken()
        if (token.isNullOrBlank() || _isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true

            when (val result = getMeUseCase(token)) {
                is ApiResult.Success -> {
                    SessionManager.updateProfile(result.data)
                    _profile.value = SessionManager.getUserProfile()
                }
                is ApiResult.Error -> {
                }
                ApiResult.Loading -> Unit
            }

            _isRefreshing.value = false
        }
    }

    /**
     * Proses refresh accessToken pakai refreshToken tersimpan (POST /auth/refresh).
     *
     * BELUM dipanggil dari UI manapun -- disiapkan dulu sesuai permintaan,
     * tinggal panggil `BerandaViewModel.refreshAccessToken { berhasil -> ... }`
     * dari tombol/trigger apapun nanti kalau sudah dibutuhkan.
     */
    fun refreshAccessToken(onResult: (success: Boolean) -> Unit = {}) {
        val refreshToken = SessionManager.getRefreshToken()
        if (refreshToken.isNullOrBlank()) {
            onResult(false)
            return
        }

        viewModelScope.launch {
            when (val result = refreshTokenUseCase(refreshToken)) {
                is ApiResult.Success -> {
                    SessionManager.updateTokens(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken,
                    )
                    onResult(true)
                }
                is ApiResult.Error -> onResult(false)
                ApiResult.Loading -> Unit
            }
        }
    }

    fun logout() {
        SessionManager.clearSession()
    }
}
