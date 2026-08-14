package bsb.dev.bsb_bangking_jp.feature.aktivitas.domain

/** Padanan ActivityFilterPayload  */
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
)