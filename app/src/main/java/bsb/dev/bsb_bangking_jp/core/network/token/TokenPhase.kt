// core/network/token/TokenPhase.kt
package bsb.dev.bsb_bangking_jp.core.network.token

enum class TokenPhase {
    INIT,
    LOGIN,
    TRANSFER,
}

data class TokenPhaseTag(val phase: TokenPhase)