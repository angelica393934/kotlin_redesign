package bsb.dev.bsb_bangking_jp.feature.beranda.viewmodel

import androidx.lifecycle.ViewModel
import bsb.dev.bsb_bangking_jp.core.session.SessionManager
import bsb.dev.bsb_bangking_jp.core.session.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * TODO: dulu ViewModel ini refresh profile lewat GET /auth/me (dummyjson) dan
 * refresh token lewat POST /auth/refresh. Endpoint itu sudah dihapus karena
 * ganti base URL & struktur API baru (lihat core/network/header/ApiHeaders.kt).
 *
 * Untuk sementara ViewModel ini HANYA baca dari SessionManager (data lokal),
 * tanpa hit API apapun. Begitu endpoint profile & refresh-token versi baru
 * sudah tersedia, tinggal suntik use case-nya lagi ke constructor & panggil
 * di refresh()/refreshAccessToken() seperti pola InitViewModel/InitDeviceUseCase.
 */
class BerandaViewModel : ViewModel() {

    private val _profile = MutableStateFlow(SessionManager.getUserProfile())
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    /**
     * Pull-to-refresh sementara cuma baca ulang data lokal dari SessionManager
     * (tidak ada network call). `isRefreshing` tetap di-toggle supaya
     * PullToRefreshBox di BerandaPage tidak perlu ubah UI logic sama sekali.
     */
    fun refresh() {
        if (_isRefreshing.value) return

        _isRefreshing.value = true
        _profile.value = SessionManager.getUserProfile()
        _isRefreshing.value = false
    }

    fun logout() {
        SessionManager.clearSession()
    }
}