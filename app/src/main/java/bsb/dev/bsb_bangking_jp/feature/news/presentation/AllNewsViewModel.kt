package bsb.dev.bsb_bangking_jp.feature.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.feature.news.domain.AllNewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllNewsViewModel(
    private val repository: AllNewsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllNewsUiState>(AllNewsUiState.Initial)
    val uiState: StateFlow<AllNewsUiState> = _uiState.asStateFlow()

    /** Padanan `_onFetchAllNews` di Bloc -- no-op kalau sudah Success, kecuali forceRefresh. */
    fun load(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value is AllNewsUiState.Success) return

        viewModelScope.launch {
            _uiState.update { AllNewsUiState.Loading }
            try {
                val items = repository.getAllNews(forceRefresh)
                _uiState.update { AllNewsUiState.Success(items) }
            } catch (e: Exception) {
                val message = (e as? ApiException)?.respMessage ?: e.message ?: "Gagal memuat berita."
                _uiState.update { AllNewsUiState.Error(message) }
            }
        }
    }

    fun retry() = load(forceRefresh = true)
}