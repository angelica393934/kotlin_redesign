package bsb.dev.bsb_bangking_jp.feature.transfer.domain.last_transfer

data class LastTransferItem(
    val accountDestination: String,
    val accountDestinationName: String,
    val bankName: String,
    val bankCode: String,
)