package bsb.dev.bsb_bangking_jp.core.components.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/** Setara `SkeletonBerita` di Flutter: card putih judul + gambar 16:9 + dot indicator. */
@Composable
fun SkeletonBerita(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        // Judul "Berita"
        SkeletonBox(width = 100.dp, height = 16.dp, borderRadius = 8.dp)
        Spacer(modifier = Modifier.height(12.dp))

        // Gambar utama rasio 16:9 (pakai shimmer brush langsung karena ukurannya
        // ditentukan oleh aspectRatio, bukan height/width tetap seperti SkeletonBox biasa)
        val imageBrush = rememberShimmerBrush()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(imageBrush),
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Indikator titik (3 dummy, tidak shimmer — dot statis seperti aslinya)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
            }
        }
    }
}
