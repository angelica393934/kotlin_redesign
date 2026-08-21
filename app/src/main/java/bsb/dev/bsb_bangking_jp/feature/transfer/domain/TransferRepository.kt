package bsb.dev.bsb_bangking_jp.feature.transfer.domain

import bsb.dev.bsb_bangking_jp.core.dummy.ConfirmTransferResult

interface TransferRepository {
    suspend fun getAccountDest(code: String, accountNumber: String): Result<TransferInquiry>
    suspend fun saveRecipient(alias: String): Result<Unit>
    suspend fun transfer(request: TransferRequestPayload): Result<TransferResult>
    suspend fun confirmTransfer(mobilePin: String): Result<ConfirmTransferResult>
    suspend fun getTransferPurpose(): Result<List<TransferPurpose>>
}

/** Padanan parameter TransferEvent.transfer() di Flutter, dikumpulkan jadi satu payload. */
data class TransferRequestPayload(
    val sourceAccountNo: String,
    val amount: Double,
    val service: String,
    val scheduleType: String,
    val frequency: String? = null,
    val endOfMonth: Boolean? = null,
    val scheduleDate: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val remark: String? = null,
    val purpose: String? = null,
)