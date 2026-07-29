package bsb.dev.bsb_bangking_jp.feature.beranda.section

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun InitialAvatar(
    initials: String,
    photoBytes: ByteArray?,
    radius: Int = 22,
    modifier: Modifier = Modifier,
) {
    val diameter = (radius * 2).dp
    Box(
        modifier = modifier
            .size(diameter)
            .clip(CircleShape)
            // TODO: sesuaikan dengan token warna avatar asli jika berbeda
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (photoBytes != null) {
            val bitmap = remember(photoBytes) {
                BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
            }
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(diameter)
                    .clip(CircleShape),
            )
        } else {
            Text(
                text = initials,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/** Padanan dari StringUtils.getInitials di Dart. */
fun getInitials(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> ""
        parts.size == 1 -> parts[0].take(1).uppercase()
        else -> (parts.first().take(1) + parts.last().take(1)).uppercase()
    }
}