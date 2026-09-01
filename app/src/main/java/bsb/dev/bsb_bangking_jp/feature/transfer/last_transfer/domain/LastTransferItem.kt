package bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.domain

data class LastTransferItem(
    val accountDestination: String,
    val accountDestinationName: String,
    val bankName: String,
    val bankCode: String,
)