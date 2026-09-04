package bsb.dev.bsb_bangking_jp.feature.message.data

import bsb.dev.bsb_bangking_jp.core.network.BaseRespCodeResponse
import bsb.dev.bsb_bangking_jp.feature.message.domain.MessageItem
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GetMessageRequest(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("fromDateTime") val fromDateTime: String? = null,
    @SerializedName("toDateTime") val toDateTime: String? = null,
    @SerializedName("quickRange") val quickRange: Int? = null,
    @SerializedName("jenis") val jenis: List<String>? = null,
    @SerializedName("category") val category: List<String>? = null,
)

data class MessageHistoryResponse(
    @SerializedName("respCode") override val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: MessageHistoryData = MessageHistoryData(),
) : BaseRespCodeResponse

data class MessageHistoryData(
    @SerializedName("number") val number: String = "",
    @SerializedName("history") val history: List<MessageHistoryItemDto> = emptyList(),
)

// TODO: sesuaikan field kalau nama key JSON asli beda -- ini mengikuti persis
// MessageHistoryItem.fromJson() versi Flutter.
data class MessageHistoryItemDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("createddate") val createdDate: String? = null,
    @SerializedName("accountdestination") val accountDestination: String = "",
    @SerializedName("note") val note: String = "",
    @SerializedName("amount") val amount: Long = 0,
    @SerializedName("total_amount") val totalAmount: Long = 0,
    @SerializedName("adminfee") val adminFee: Long = 0,
    @SerializedName("status") val status: String = "",
    @SerializedName("jenis_transaksi") val jenisTransaksi: String = "",
    @SerializedName("kategori") val kategori: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("bankname") val bankName: String = "",
    @SerializedName("bank_code") val bankCode: String = "",
    @SerializedName("transaction_id") val transactionId: Int = 0,
    @SerializedName("ScheduledTransferID") val scheduledTransferId: Int? = null,
)

private val isoDateTimeParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

fun MessageHistoryItemDto.toDomain(): MessageItem = MessageItem(
    id = id,
    createdDate = createdDate?.let { runCatching { isoDateTimeParser.parse(it.take(19)) }.getOrNull() } ?: Date(0),
    accountDestination = accountDestination,
    note = note,
    amount = amount,
    totalAmount = totalAmount,
    adminFee = adminFee,
    status = status,
    jenisTransaksi = jenisTransaksi,
    kategori = kategori,
    type = type,
    bankName = bankName,
    bankCode = bankCode,
    transactionId = transactionId,
    scheduledTransferId = scheduledTransferId,
)