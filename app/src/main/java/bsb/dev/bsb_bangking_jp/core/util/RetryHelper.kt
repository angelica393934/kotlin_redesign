package bsb.dev.bsb_bangking_jp.core.util

import kotlinx.coroutines.delay

/**
 * Delay bisa disesuaikan per pemanggilan.
 */
suspend fun <T> retry(
    maxAttempt: Int = 1,
    delayMillis: Long = 5000,
    task: suspend () -> T,
): T {
    var lastError: Throwable? = null
    repeat(maxAttempt) { attempt ->
        try {
            return task()
        } catch (e: Exception) {
            lastError = e
            if (attempt < maxAttempt - 1) delay(delayMillis)
        }
    }
    throw lastError ?: IllegalStateException("Retry gagal tanpa error yang tercatat.")
}