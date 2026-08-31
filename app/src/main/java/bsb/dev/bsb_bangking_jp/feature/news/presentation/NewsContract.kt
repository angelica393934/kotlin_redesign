package bsb.dev.bsb_bangking_jp.feature.news.presentation

import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsItem

sealed interface NewsUiState {
    data object Initial : NewsUiState
    data object Loading : NewsUiState
    data class Success(val items: List<NewsItem>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}