package bsb.dev.bsb_bangking_jp.feature.portal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.session.SessionManager
import bsb.dev.bsb_bangking_jp.core.util.ApiResult
import bsb.dev.bsb_bangking_jp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * State UI untuk LoginSheet. Form (username/password) tetap dikelola sebagai
 * `remember` di Composable seperti sekarang -- ViewModel hanya menyimpan
 * status request (loading/error/sukses) supaya survive recomposition &
 * config change (mis. rotasi layar saat request masih berjalan).
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false,
)

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Username dan password wajib diisi",
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = repository.login(username, password)) {
                is ApiResult.Success -> {
                    SessionManager.saveSession(
                        accessToken = result.data.accessToken,
                        username = result.data.username,
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccess = true,
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                    )
                }
                ApiResult.Loading -> Unit
            }
        }
    }

    /** Dipanggil sesudah navigasi ke navbar dijalankan, supaya tidak trigger berulang. */
    fun onNavigated() {
        _uiState.value = _uiState.value.copy(isLoginSuccess = false)
    }

    /** Dipanggil sesudah error ditampilkan (mis. lewat Toast). */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
