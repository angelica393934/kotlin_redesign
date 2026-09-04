package bsb.dev.bsb_bangking_jp.core.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.core.theme.LocalExtendedColors

/**
 * Setara `SkeletonBanner` di : banner solid (bukan shimmer) dengan
 * kotak shimmer di dalamnya + tombol close.
 * Warna background pakai `ExtendedColors.warning` dari tema project (ganti sesuai kebutuhan).
 */
@Composable
fun SkeletonBanner(modifier: Modifier = Modifier) {
    val extendedColors = LocalExtendedColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(extendedColors.warning)
            .padding(10.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            SkeletonBox(height = 14.dp, width = 140.dp, borderRadius = 6.dp)
            Spacer(modifier = Modifier.height(6.dp))
            SkeletonBox(height = 12.dp, width = 200.dp, borderRadius = 6.dp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Tombol close (X)
        SkeletonBox(
            height = 18.dp,
            width = 18.dp,
            borderRadius = 4.dp,
            modifier = Modifier,
        )
    }
}
