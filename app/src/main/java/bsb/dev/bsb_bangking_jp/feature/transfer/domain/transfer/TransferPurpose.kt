package bsb.dev.bsb_bangking_jp.feature.transfer.domain.transfer

/** Padanan item hasil GET /v1/gettransferpurpose. */
data class TransferPurpose(
    val code: String,
    val name: String,
)