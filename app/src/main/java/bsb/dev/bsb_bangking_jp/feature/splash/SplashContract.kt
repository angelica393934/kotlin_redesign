package bsb.dev.bsb_bangking_jp.feature.splash

sealed class SplashUiState {
    object Loading : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}

/** One-shot event supaya navigasi cuma terjadi sekali. */
sealed class SplashNavigationEvent {
    object ToIntro : SplashNavigationEvent()
    object ToPortal : SplashNavigationEvent()
}