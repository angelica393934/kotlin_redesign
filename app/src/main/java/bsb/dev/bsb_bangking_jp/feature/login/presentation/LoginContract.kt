package bsb.dev.bsb_bangking_jp.feature.login.presentation

data class LoginUiState(
    val useridLogin: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val useridError: String? = null,
    val passcodeError: String? = null,
)

sealed class LoginNavEvent {
    object ToNavbar : LoginNavEvent()
}

sealed class LoginUiEvent {
    data class ShowToastError(val message: String) : LoginUiEvent()
}