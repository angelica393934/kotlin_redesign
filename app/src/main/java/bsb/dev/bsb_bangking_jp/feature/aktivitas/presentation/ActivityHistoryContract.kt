package bsb.dev.bsb_bangking_jp.feature.aktivitas.presentation

import bsb.dev.bsb_bangking_jp.feature.aktivitas.data.HistoryItem
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityFilterPayload

data class ActivityHistoryUiState(
    val isLoading: Boolean = false,
    val isLoadMore: Boolean = false,
    val items: List<HistoryItem> = emptyList(),
    val hasMore: Boolean = true,
    val error: String? = null,
    val activeFilter: ActivityFilterPayload? = null,
)