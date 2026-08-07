// feature/login_existing/presentation/LoginExistingContract.kt
package bsb.dev.bsb_bangking_jp.feature.login_existing.presentation

data class LoginExistingUiState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val phoneInlineError: String? = null,
    val otpErrorMessage: String? = null,
    val confirmPinError: String? = null,
)

sealed class LoginExistingNavEvent {
    object ToOtpPage : LoginExistingNavEvent()
    object ToPinPage : LoginExistingNavEvent()
    object ToPortal : LoginExistingNavEvent()
}

sealed class LoginExistingUiEvent {
    data class ShowToastError(val message: String) : LoginExistingUiEvent()
    object ShowOtpResentToast : LoginExistingUiEvent()
}