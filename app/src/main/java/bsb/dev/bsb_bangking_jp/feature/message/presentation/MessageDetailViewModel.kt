package bsb.dev.bsb_bangking_jp.feature.message.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.feature.message.domain.MessageDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "MessageDetail"

sealed interface MessageDetailUiState {
    data object Initial : MessageDetailUiState
    data object Loading : MessageDetailUiState
    data class Success(val rawJson: String) : MessageDetailUiState
    data class Error(val message: String) : MessageDetailUiState
}

class MessageDetailViewModel(
    private val repository: MessageDetailRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MessageDetailUiState>(MessageDetailUiState.Initial)
    val uiState: StateFlow<MessageDetailUiState> = _uiState.asStateFlow()

    fun load(id: Int) {
        viewModelScope.launch {
            _uiState.update { MessageDetailUiState.Loading }
            try {
                val rawJson = repository.getMessageDetailRaw(id)
                Log.d(TAG, "getmessagebyid($id) response.data =\n$rawJson")
                _uiState.update { MessageDetailUiState.Success(rawJson) }
            } catch (e: Exception) {
                val message = (e as? ApiException)?.respMessage ?: e.message ?: "Gagal memuat detail message."
                Log.e(TAG, "getmessagebyid($id) FAILED", e)
                _uiState.update { MessageDetailUiState.Error(message) }
            }
        }
    }

    fun reset() {
        _uiState.update { MessageDetailUiState.Initial }
    }
}