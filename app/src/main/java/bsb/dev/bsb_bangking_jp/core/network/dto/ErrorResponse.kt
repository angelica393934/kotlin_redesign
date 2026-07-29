package bsb.dev.bsb_bangking_jp.core.network.dto

/**
 * Bentuk body error dari dummyjson.com, mis. saat 400/401:
 *   { "message": "Invalid credentials" }
 */
data class ErrorResponse(
    val message: String?,
)
