package bsb.dev.bsb_bangking_jp.shared.logout.presentation

sealed class LogoutUiState {
    data object Initial : LogoutUiState()
    data object Loading : LogoutUiState()
    data object Success : LogoutUiState()
    data class Failure(val respCode: String, val respMessage: String) : LogoutUiState()
}