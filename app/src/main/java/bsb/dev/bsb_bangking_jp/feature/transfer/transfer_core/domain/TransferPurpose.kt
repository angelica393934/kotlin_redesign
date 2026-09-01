package bsb.dev.bsb_bangking_jp.feature.transfer.transfer_core.domain

/** Padanan item hasil GET /v1/gettransferpurpose. */
data class TransferPurpose(
    val code: String,
    val name: String,
)