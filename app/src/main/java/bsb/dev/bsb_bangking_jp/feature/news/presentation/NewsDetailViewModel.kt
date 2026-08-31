package bsb.dev.bsb_bangking_jp.feature.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsDetail
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface NewsDetailUiState {
    data object Initial : NewsDetailUiState
    data object Loading : NewsDetailUiState
    data class Success(val data: NewsDetail) : NewsDetailUiState
    data class Error(val message: String) : NewsDetailUiState
}

class NewsDetailViewModel(
    private val repository: NewsDetailRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsDetailUiState>(NewsDetailUiState.Initial)
    val uiState: StateFlow<NewsDetailUiState> = _uiState.asStateFlow()

    fun load(id: Int, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { NewsDetailUiState.Loading }
            try {
                val detail = repository.getNewsDetail(id, forceRefresh)
                _uiState.update { NewsDetailUiState.Success(detail) }
            } catch (e: Exception) {
                val message = (e as? ApiException)?.respMessage ?: e.message ?: "Gagal memuat detail berita."
                _uiState.update { NewsDetailUiState.Error(message) }
            }
        }
    }
}