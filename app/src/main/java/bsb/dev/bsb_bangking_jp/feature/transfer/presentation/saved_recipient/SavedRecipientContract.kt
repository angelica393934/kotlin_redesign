package bsb.dev.bsb_bangking_jp.feature.transfer.presentation.saved_recipient

import bsb.dev.bsb_bangking_jp.feature.transfer.domain.saved_recipient.SavedRecipientItem

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