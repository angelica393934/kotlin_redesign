package bsb.dev.bsb_bangking_jp.core.util

object CurrencyUtils {

    fun formatRupiah(amount: Int): String {
        val formatted = "%,d".format(amount).replace(",", ".")
        return "Rp $formatted"
    }
}