package bsb.dev.bsb_bangking_jp.core.util

    fun RupiahFormat(amount: Int): String {
        val formatted = "%,d".format(amount).replace(",", ".")
        return "Rp $formatted"
    }