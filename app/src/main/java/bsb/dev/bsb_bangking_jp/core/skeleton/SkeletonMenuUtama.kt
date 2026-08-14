package bsb.dev.bsb_bangking_jp.core.components.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Setara `SkeletonMenuUtama` di Flutter: judul + grid 4 kolom berisi [SkeletonMenu].
 * `Wrap` di Flutter -> [FlowRow] di Compose (`maxItemsInEachRow = 4` = 4 kolom per baris).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkeletonMenuUtama(
    modifier: Modifier = Modifier,
    itemCount: Int = 8,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 18.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        // Judul
        SkeletonBox(width = 120.dp, height = 18.dp, borderRadius = 8.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Grid skeleton (4 kolom per baris)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 4,
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            repeat(itemCount) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f / 4f)
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    SkeletonMenu()
                }
            }
        }
    }
}
