package bsb.dev.bsb_bangking_jp.core.components.skeleton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Setara `HaloUserSkeleton` di Flutter: avatar + "Halo, Nama" di kiri, 2 ikon di kanan. */
@Composable
fun HaloUserSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Bagian kiri: avatar + teks
        Row(verticalAlignment = Alignment.CenterVertically) {
            SkeletonAvatar(size = 55.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                SkeletonBox(height = 12.dp, width = 50.dp) // "Halo,"
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(height = 18.dp, width = 140.dp) // Nama user
            }
        }

        // Bagian kanan: ikon-ikon (mis. notifikasi)
        Row {
            SkeletonBox(height = 35.dp, width = 35.dp, borderRadius = 10.dp)
            Spacer(modifier = Modifier.width(10.dp))
            SkeletonBox(height = 35.dp, width = 35.dp, borderRadius = 10.dp)
        }
    }
}
