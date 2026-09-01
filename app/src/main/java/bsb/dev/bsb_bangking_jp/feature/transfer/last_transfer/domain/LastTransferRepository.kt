package bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.domain

interface LastTransferRepository {
    suspend fun getLastTransfer(forceRefresh: Boolean = false): List<LastTransferItem>
}