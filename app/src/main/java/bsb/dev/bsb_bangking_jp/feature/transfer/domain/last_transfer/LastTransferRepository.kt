package bsb.dev.bsb_bangking_jp.feature.transfer.domain.last_transfer

interface LastTransferRepository {
    suspend fun getLastTransfer(forceRefresh: Boolean = false): List<LastTransferItem>
}