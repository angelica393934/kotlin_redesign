package bsb.dev.bsb_bangking_jp.shared.get_image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import bsb.dev.bsb_bangking_jp.shared.get_image.domain.ImageCategory
import bsb.dev.bsb_bangking_jp.shared.get_image.domain.ImageRepository
import org.koin.compose.koinInject

sealed interface NetworkImageState {
    data object Loading : NetworkImageState
    data class Loaded(val bytes: ByteArray) : NetworkImageState
    data object Failed : NetworkImageState
}

@Composable
fun rememberNetworkImageState(
    path: String?,
    category: ImageCategory,
    imageRepository: ImageRepository = koinInject(),
): NetworkImageState {
    val state by produceState<NetworkImageState>(
        initialValue = NetworkImageState.Loading,
        key1 = path,
        key2 = category,
    ) {
        if (path.isNullOrEmpty()) {
            value = NetworkImageState.Failed
            return@produceState
        }

        val bytes = imageRepository.getImage(path, category)
        value = if (bytes != null) NetworkImageState.Loaded(bytes) else NetworkImageState.Failed
    }
    return state
}