package bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.domain

data class SavedRecipientItem(
    val id: String,
    val accountNumber: String,
    val alias: String,
    val bankName: String,
    val accountName: String? = null,
    val bankCode: String,
)

interface SavedRecipientRepository {
    val hasData: Boolean
    val cachedData: List<SavedRecipientItem>?
    suspend fun getSavedRecipients(forceRefresh: Boolean = false): List<SavedRecipientItem>
    suspend fun updateSavedRecipient(id: String, alias: String): Result<Unit>
    suspend fun deleteSavedRecipient(ids: List<String>): Result<Unit>
}