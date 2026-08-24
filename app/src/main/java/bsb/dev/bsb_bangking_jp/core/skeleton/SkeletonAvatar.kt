package bsb.dev.bsb_bangking_jp.core.skeleton

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wrapper tipis di atas [SkeletonBox] khusus avatar bulat.
 *
 * Ini SEPENUHNYA opsional — kalau tidak mau nambah 1 file lagi,
 * kamu bisa langsung pakai:
 *   SkeletonBox(height = 50.dp, width = 50.dp, isCircular = true)
 * di tempat kamu biasa pakai SkeletonAvatar(size = 50.dp).
 * Wrapper ini cuma bikin pemanggilan lebih deskriptif.
 */
@Composable
fun SkeletonAvatar(
    size: Dp = 50.dp,
    modifier: Modifier = Modifier,
) {
    SkeletonBox(
        height = size,
        width = size,
        isCircular = true,
        modifier = modifier,
    )
}
