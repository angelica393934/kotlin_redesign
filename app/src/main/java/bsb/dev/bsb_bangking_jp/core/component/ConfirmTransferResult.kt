package bsb.dev.bsb_bangking_jp.core.dummy

import java.util.Date

/**
 * Padanan `ConfirmTransferResultModel` (Flutter) -- hasil dari API confirm-transfer.
 *
 * TODO: ganti jadi model response API asli begitu backend transfer sudah terintegrasi. Struktur
 * field di bawah disesuaikan dengan field yang dipakai `TransferBerhasilPage` &
 * `TransferBerhasilDijadwalkanPage` di versi Flutter.
 */
data class ConfirmTransferResult(
    val reffNum: String,
    val transactionDate: Date,
    val beneficiaryName: String,
    val beneficiaryBankName: String,
    val beneficiaryAccountNo: String,
    val senderName: String,
    val senderAccountNo: String,
    val amount: Int,
    val adminFee: Int,
    val totalDebit: Int,
    val remark: String?,
    /** "IMMEDIATE" atau "SCHEDULED". */
    val scheduleType: String,
    /** "ONCE" atau "MONTHLY" -- null kalau scheduleType == "IMMEDIATE". */
    val frequency: String?,
    val scheduleDate: String?,
    val startMonth: String?,
    val endMonth: String?,
)