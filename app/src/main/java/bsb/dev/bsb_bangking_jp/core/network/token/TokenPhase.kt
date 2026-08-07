// core/network/token/TokenPhase.kt
package bsb.dev.bsb_bangking_jp.core.network.token

enum class TokenPhase {
    INIT,
    // TODO: tambah LOGIN, REGIST, dll di sini saat flow itu dibuat -- perluas
    // getStoredAccessToken()/tryRefresh() di TokenRefreshInterceptor kalau ditambah.
}

data class TokenPhaseTag(val phase: TokenPhase)