package bsb.dev.bsb_bangking_jp.feature.news.presentation

import bsb.dev.bsb_bangking_jp.feature.news.domain.AllNewsItem

sealed interface AllNewsUiState {
    data object Initial : AllNewsUiState
    data object Loading : AllNewsUiState
    data class Success(val items: List<AllNewsItem>) : AllNewsUiState
    data class Error(val message: String) : AllNewsUiState
}