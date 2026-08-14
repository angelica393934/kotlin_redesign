package bsb.dev.bsb_bangking_jp.feature.aktivitas.domain

import bsb.dev.bsb_bangking_jp.feature.aktivitas.data.HistoryItem

interface ActivityHistoryRepository {
    val hasMore: Boolean
    val items: List<HistoryItem>

    suspend fun loadInitial(accountNumber: String, filter: ActivityFilterPayload): List<HistoryItem>
    suspend fun loadMore(accountNumber: String, filter: ActivityFilterPayload): List<HistoryItem>
}