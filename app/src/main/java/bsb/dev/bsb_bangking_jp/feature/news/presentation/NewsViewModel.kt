package bsb.dev.bsb_bangking_jp.feature.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Initial)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    fun load(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { NewsUiState.Loading }
            try {
                val items = repository.getNews(forceRefresh)
                _uiState.update { NewsUiState.Success(items) }
            } catch (e: Exception) {
                val message = (e as? ApiException)?.respMessage ?: e.message ?: "Gagal memuat berita."
                _uiState.update { NewsUiState.Error(message) }
            }
        }
    }
}