package bsb.dev.bsb_bangking_jp.feature.aktivitas.domain

import bsb.dev.bsb_bangking_jp.core.util.DefaultRangeDate

data class ActivityFilterPayload(
    val fromDate: String? = null,
    val toDate: String? = null,
    val quickRange: Int? = null,
    val jenis: List<String>? = null,
    val category: List<String>? = null,
    val isAllJenis: Boolean = true,
    val isAllCategory: Boolean = true,
    val isDefaultDate: Boolean = true,
    val label: String = "",
) {
    companion object {
        /** Padanan ActivityFilterPayload.initial() -- satu-satunya default yang sah. */
        fun initial(): ActivityFilterPayload {
            val def = DefaultRangeDate.getCurrentMonth()
            return ActivityFilterPayload(
                fromDate = def.from,
                toDate = def.to,
                label = "Semua Transaksi",
            )
        }
    }

    /**
     * Padanan copyWith() dengan reset-flag di . Kotlin `data class.copy()` tidak bisa
     * bedakan "parameter tidak diisi" vs "sengaja diisi null", jadi dipakai flag reset eksplisit
     * sama seperti versi Dart-nya.
     */
    fun with(
        fromDate: String? = this.fromDate,
        resetFromDate: Boolean = false,
        toDate: String? = this.toDate,
        resetToDate: Boolean = false,
        quickRange: Int? = this.quickRange,
        resetQuickRange: Boolean = false,
        jenis: List<String>? = this.jenis,
        resetJenis: Boolean = false,
        category: List<String>? = this.category,
        resetCategory: Boolean = false,
        isAllJenis: Boolean = this.isAllJenis,
        isAllCategory: Boolean = this.isAllCategory,
        isDefaultDate: Boolean = this.isDefaultDate,
        label: String = this.label,
    ): ActivityFilterPayload = ActivityFilterPayload(
        fromDate = if (resetFromDate) null else fromDate,
        toDate = if (resetToDate) null else toDate,
        quickRange = if (resetQuickRange) null else quickRange,
        jenis = if (resetJenis) null else jenis,
        category = if (resetCategory) null else category,
        isAllJenis = isAllJenis,
        isAllCategory = isAllCategory,
        isDefaultDate = isDefaultDate,
        label = label,
    )
}