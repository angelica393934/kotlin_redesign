package bsb.dev.bsb_bangking_jp.core.util

/** Menutupi bagian tengah nomor rekening dengan "*", mis. "12****90". */
fun maskAccountNumber(number: String): String {
    if (number.length <= 4) return number
    val visibleStart = number.take(2)
    val visibleEnd = number.takeLast(2)
    return "$visibleStart${"*".repeat(number.length - 4)}$visibleEnd"
}