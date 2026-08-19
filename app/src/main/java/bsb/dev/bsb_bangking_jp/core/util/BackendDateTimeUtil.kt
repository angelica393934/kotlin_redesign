package bsb.dev.bsb_bangking_jp.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Konversi tanggal sederhana ("yyyy-MM-dd", dipakai internal di ActivityFilterPayload/UI)
 * jadi format datetime lengkap yang diharapkan backend untuk field fromDateTime/toDateTime.
 *
 * Pola formatter disamakan persis dengan ApiHeaders.currentTimestamp()
 * ("yyyy-MM-dd'T'HH:mm:ss'+07:00'" -- offset ditulis sebagai literal string, BUKAN dihitung
 * dari timezone device saat runtime).
 */
object BackendDateTimeUtil {
    private val inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+07:00'")

    /** Awal hari (00:00:00), dipakai untuk fromDateTime -- sama untuk default maupun manual. */
    fun startOfDay(dateString: String): String {
        val date = LocalDate.parse(dateString, inputDateFormatter)
        return LocalDateTime.of(date, LocalTime.MIN).format(outputFormatter)
    }

    /** Akhir hari (23:59:59), dipakai untuk toDateTime saat RENTANG MANUAL dipilih user. */
    fun endOfDay(dateString: String): String {
        val date = LocalDate.parse(dateString, inputDateFormatter)
        return LocalDateTime.of(date, LocalTime.of(23, 59, 59)).format(outputFormatter)
    }

    /**
     * Waktu saat ini (bukan akhir hari), dipakai untuk toDateTime saat filter masih DEFAULT
     * (rentang bulan berjalan) -- supaya "sampai" berarti "sampai sekarang", bukan sampai
     * jam 23:59 hari ini yang belum terjadi.
     */
    fun now(): String = LocalDateTime.now().format(outputFormatter)
}