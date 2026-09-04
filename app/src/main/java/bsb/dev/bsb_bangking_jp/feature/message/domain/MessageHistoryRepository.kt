package bsb.dev.bsb_bangking_jp.feature.message.domain

import bsb.dev.bsb_bangking_jp.core.filter.TransactionFilterPayload

interface MessageHistoryRepository {
    val hasMore: Boolean
    val items: List<MessageItem>

    suspend fun loadInitial(accountNumber: String, filter: TransactionFilterPayload): List<MessageItem>
    suspend fun loadMore(accountNumber: String, filter: TransactionFilterPayload): List<MessageItem>
}