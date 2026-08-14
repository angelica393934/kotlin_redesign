package bsb.dev.bsb_bangking_jp.core.util
// ini karena date format backend masih berantakan.

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DateRange(val from: String, val to: String)

object DefaultRangeDate {
    private val backendFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /** Padanan DefaultRangeDate.getCurrentMonth() -- awal bulan s.d. hari ini. */
    fun getCurrentMonth(): DateRange {
        val today = LocalDate.now()
        val firstOfMonth = today.withDayOfMonth(1)
        return DateRange(
            from = firstOfMonth.format(backendFormatter),
            to = today.format(backendFormatter),
        )
    }
}