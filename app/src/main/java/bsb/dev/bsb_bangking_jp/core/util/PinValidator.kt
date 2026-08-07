// core/util/PinValidator.kt
package bsb.dev.bsb_bangking_jp.core.util

object PinValidator {
    fun validateNewPin(pin: String): String? {
        if (!Regex("^[0-9]{6}$").matches(pin)) return "M-PIN harus terdiri dari 6 angka."

        var seq = 1
        var repeat = 1
        for (i in 1 until pin.length) {
            val prev = pin[i - 1] - '0'
            val curr = pin[i] - '0'
            seq = if (curr == prev + 1 || curr == prev - 1) seq + 1 else 1
            repeat = if (curr == prev) repeat + 1 else 1
            if (seq >= 3) return "M-PIN tidak boleh berurutan, seperti 123 atau 321."
            if (repeat >= 3) return "M-PIN tidak boleh memiliki angka sama 3x, seperti 111."
        }
        return null
    }
}