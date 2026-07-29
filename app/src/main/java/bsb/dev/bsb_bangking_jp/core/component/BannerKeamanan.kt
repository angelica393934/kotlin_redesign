package bsb.dev.bsb_bangking_jp.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.theme.Orange500

/**
 * Padanan dari widgets/banner_keamanan.dart
 * Banner peringatan keamanan yang bisa ditutup (dismissible), state
 * visibilitasnya lokal (StatefulWidget -> remember di Compose).
 */
@Composable
fun BannerKeamanan(
    title: String? = stringResource(R.string.banner_keamanan_title),
    desc: String = stringResource(R.string.banner_keamanan_desc),
    boldDesc: Boolean = false,
    // null -> pakai warna default (Orange500), sama seperti `backgroundColor` opsional di Dart
    backgroundColor: Color? = null,
    modifier: Modifier = Modifier,
) {
    var isVisible by rememberSaveable { mutableStateOf(true) }

    if (!isVisible) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor ?: Orange500)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (!title.isNullOrEmpty()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                    )
                }
                Text(
                    text = desc,
                    fontSize = 10.sp,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { isVisible = false },
                modifier = Modifier.size(18.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.cd_tutup_banner),
                    tint = Color.White,
                )
            }
        }
    }
}