package bsb.dev.bsb_bangking_jp.feature.transfer.domain.transfer

import java.util.Date

/** Padanan TransferResultModel -- hasil dari POST /v1/dashboard/transfer. */
data class TransferResult(
    val referenceId: String,
    val amount: Double,
    val totalDebit: Double,
    val schedule: String,
    val serviceType: String,
    val sourceAccount: String,
    val nextRunDate: Date?,
    val beneficiary: TransferResultBeneficiary,
    val adminFee: List<TransferAdminFee>,
)

data class TransferResultBeneficiary(
    val accountName: String,
    val accountNo: String,
    val bankName: String,
    val bankCode: String,
)

data class TransferAdminFee(
    val amount: Double,
    val trxType: String,
)