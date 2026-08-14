package bsb.dev.bsb_bangking_jp.feature.aktivitas.data

import com.google.gson.annotations.SerializedName

data class GetHistoryRequest(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("fromDateTime") val fromDateTime: String? = null,
    @SerializedName("toDateTime") val toDateTime: String? = null,
    @SerializedName("quickRange") val quickRange: Int? = null,
    @SerializedName("jenis") val jenis: List<String>? = null,
    @SerializedName("category") val category: List<String>? = null,
)

data class ActivityHistoryResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: ActivityHistoryData = ActivityHistoryData(),
)

/** Padanan bagian `data` di ActivityHistoryModel.fromJson -- berisi nomor rekening + list histori. */
data class ActivityHistoryData(
    @SerializedName("number") val number: String = "",
    @SerializedName("history") val history: List<HistoryItem> = emptyList(),
)

data class HistoryAmount(
    @SerializedName("currency") val currency: String = "",
    @SerializedName("value") val value: String = "",
)

data class HistoryDetailBalance(
    @SerializedName("Amount") val amount: HistoryAmount = HistoryAmount(),
)

data class HistoryItem(
    @SerializedName("detailBalance") val detailBalance: HistoryDetailBalance = HistoryDetailBalance(),
    @SerializedName("remark") val remark: String = "",
    @SerializedName("transactionDate") val transactionDate: String = "", // format "yyMMdd"
    @SerializedName("transactionDetailStatus") val transactionDetailStatus: String = "",
    @SerializedName("transactionId") val transactionId: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("transactionType") val transactionType: String = "",
    @SerializedName("jenisTransaksi") val jenisTransaksi: String = "",
    @SerializedName("kategoriTransaksi") val kategoriTransaksi: String = "",
) {
    val currency: String get() = detailBalance.amount.currency
    val amount: String get() = detailBalance.amount.value

    /** Padanan getter isMasuk -- true kalau kategoriTransaksi persis "berhasil, masuk". */
    val isMasuk: Boolean get() = kategoriTransaksi.lowercase() == "berhasil, masuk"

    /** Padanan getter arahTransaksi -- ambil kata terakhir setelah koma ("masuk"/"keluar") -> "dari"/"ke". */
    val arahTransaksi: String get() {
        if (kategoriTransaksi.isEmpty()) return ""
        val parts = kategoriTransaksi.split(",")
        if (parts.size < 2) return ""
        return when (parts.last().trim().lowercase()) {
            "masuk" -> "dari"
            "keluar" -> "ke"
            else -> ""
        }
    }

    /** Padanan getter rekeningTujuan -- ambil angka setelah huruf 'S' terakhir di remark, sampai sebelum '-'. */
    val rekeningTujuan: String get() {
        if (remark.isEmpty()) return ""
        val indexS = remark.lastIndexOf('S')
        if (indexS == -1) return ""
        val afterS = remark.substring(indexS + 1)
        val indexStrip = afterS.indexOf('-')
        return if (indexStrip != -1) afterS.substring(0, indexStrip).trim() else afterS.trim()
    }

    /** Padanan getter deskripsiTransaksi -- "Jenis arah\nrekeningTujuan". */
    val deskripsiTransaksi: String get() {
        val jenis = if (jenisTransaksi.isNotEmpty()) {
            jenisTransaksi[0].uppercase() + jenisTransaksi.substring(1).lowercase()
        } else ""
        val arah = arahTransaksi
        val rekening = rekeningTujuan
        if (jenis.isEmpty() && rekening.isEmpty()) return ""
        return "$jenis $arah\n$rekening"
    }
}