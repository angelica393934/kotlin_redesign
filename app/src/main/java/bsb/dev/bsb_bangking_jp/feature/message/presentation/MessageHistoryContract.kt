package bsb.dev.bsb_bangking_jp.feature.message.presentation

import bsb.dev.bsb_bangking_jp.core.filter.TransactionFilterPayload
import bsb.dev.bsb_bangking_jp.feature.message.domain.MessageItem

data class MessageHistoryUiState(
    val accountNumber: String? = null,
    val isLoading: Boolean = false,
    val isLoadMore: Boolean = false,
    val items: List<MessageItem> = emptyList(),
    val hasMore: Boolean = true,
    val error: String? = null,
    val activeFilter: TransactionFilterPayload? = null,
)