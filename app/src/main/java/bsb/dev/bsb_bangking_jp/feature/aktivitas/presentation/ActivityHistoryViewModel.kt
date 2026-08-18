package bsb.dev.bsb_bangking_jp.feature.aktivitas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.util.DefaultRangeDate
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityFilterPayload
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivityHistoryViewModel(
    private val repository: ActivityHistoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityHistoryUiState())
    val uiState: StateFlow<ActivityHistoryUiState> = _uiState.asStateFlow()

    /** Padanan ActivityHistoryEvent.getInitial -- dipanggil begitu accountNumber pertama kali diketahui / berganti. */
    fun getInitial(accountNumber: String) {
        load(accountNumber, ActivityFilterPayload.initial())
    }

    /** Padanan ActivityHistoryEvent.applyFilter. */
    fun applyFilter(accountNumber: String, filter: ActivityFilterPayload) {
        load(accountNumber, normalizeFilter(filter))
    }

    /** Padanan ActivityHistoryEvent.refresh -- pakai filter aktif yang sedang berlaku. */
    fun refresh(accountNumber: String) {
        val filter = _uiState.value.activeFilter ?: return
        load(accountNumber, filter)
    }

    /** Padanan ActivityHistoryEvent.loadMore. */
    fun loadMore(accountNumber: String) {
        val state = _uiState.value
        if (state.isLoadMore || !repository.hasMore) return
        val filter = state.activeFilter ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadMore = true) }
            try {
                val items = repository.loadMore(accountNumber, filter)
                _uiState.update { it.copy(isLoadMore = false, items = items, hasMore = repository.hasMore) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadMore = false, error = errorMessage(e)) }
            }
        }
    }

    private fun load(accountNumber: String, filter: ActivityFilterPayload) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, isLoadMore = false, activeFilter = filter, error = null)
            }
            try {
                val items = repository.loadInitial(accountNumber, filter)
                _uiState.update { it.copy(isLoading = false, items = items, hasMore = repository.hasMore) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = errorMessage(e)) }
            }
        }
    }

    private fun errorMessage(e: Exception): String =
        (e as? ApiException)?.respMessage ?: e.message ?: "Terjadi kesalahan, silakan coba lagi."

    /** Padanan _normalizeFilter -- quickRange dan manual date saling eksklusif. */
    private fun normalizeFilter(filter: ActivityFilterPayload): ActivityFilterPayload {
        if (filter.quickRange != null) {
            return filter.with(resetFromDate = true, resetToDate = true)
        }
        if (filter.fromDate != null && filter.toDate != null) {
            return filter.with(resetQuickRange = true)
        }
        val def = DefaultRangeDate.getCurrentMonth()
        return filter.with(fromDate = def.from, toDate = def.to, resetQuickRange = true)
    }
}