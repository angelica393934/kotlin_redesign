package bsb.dev.bsb_bangking_jp.core.skeleton

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Setara `SkeletonMenu` di : avatar bulat + label kecil di bawahnya. */
@Composable
fun SkeletonMenu(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SkeletonAvatar(size = 50.dp)
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonBox(height = 12.dp, width = 60.dp, borderRadius = 6.dp)
    }
}
