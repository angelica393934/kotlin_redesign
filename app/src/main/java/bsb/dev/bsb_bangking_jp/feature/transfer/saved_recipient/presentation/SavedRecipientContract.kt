package bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.presentation

import bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.domain.SavedRecipientItem

data class SavedRecipientUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val list: List<SavedRecipientItem>? = null,
    val error: String? = null,
    val isUpdating: Boolean = false,
    val updateError: String? = null,
    val isDeleting: Boolean = false,
)

sealed class SavedRecipientUiEvent {
    data class ShowToastSuccess(val message: String) : SavedRecipientUiEvent()
    data class ShowToastError(val message: String) : SavedRecipientUiEvent()
    object AliasUpdated : SavedRecipientUiEvent()
}