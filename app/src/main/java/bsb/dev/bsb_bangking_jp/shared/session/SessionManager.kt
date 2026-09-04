package bsb.dev.bsb_bangking_jp.shared.session

import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.session.SessionClearer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Padanan SessionManager.dart -- satu pintu untuk membersihkan seluruh sesi login:
 * hapus token ("app/id/sessionModule" -> KEY_LOGIN_ACCESS_TOKEN & KEY_LOGIN_REFRESH_TOKEN
 * di SecureStorageService), bersihkan cache semua repository (via SessionClearer yang
 * sudah ada), dan update status login global.
 */
class SessionManager(
    private val secureStorage: SecureStorageService,
    private val sessionClearer: SessionClearer,
) {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun setLoggedIn(value: Boolean) {
        _isLoggedIn.value = value
    }

    /** Padanan clearSession() -- WAJIB dipanggil setelah API logout sukses. */
    fun clearSession() {
        // 1) hapus token login tersimpan
        secureStorage.clearLoginTokens()

        // 2) bersihkan cache SEMUA repository yang terdaftar (ClearableRepository)
        sessionClearer.clearAll()

        // 3) update auth state global
        _isLoggedIn.value = false
    }
}