package bsb.dev.bsb_bangking_jp.core.get_image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import bsb.dev.bsb_bangking_jp.core.get_image.domain.ImageRepository
import org.koin.compose.koinInject

sealed interface NetworkImageState {
    data object Loading : NetworkImageState
    data class Loaded(val bytes: ByteArray) : NetworkImageState
    data object Failed : NetworkImageState
}

@Composable
fun rememberNetworkImageState(
    path: String?,
    imageRepository: ImageRepository = koinInject(),
): NetworkImageState {
    val state by produceState<NetworkImageState>(
        initialValue = NetworkImageState.Loading,
        key1 = path,
    ) {
        value = if (path.isNullOrEmpty()) {
            NetworkImageState.Failed
        } else {
            val bytes = imageRepository.getImage(path)
            if (bytes != null) NetworkImageState.Loaded(bytes) else NetworkImageState.Failed
        }
    }
    return state
}