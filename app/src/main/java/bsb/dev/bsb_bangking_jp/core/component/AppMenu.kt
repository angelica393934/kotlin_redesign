package bsb.dev.bsb_bangking_jp.core.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppMenu(
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    @DrawableRes iconResId: Int? = null,
    width: Dp = 80.dp,
    circleSize: Dp = 60.dp,
    scale: Float? = null,
    useThemeStyle: Boolean = false,
    onTap: (() -> Unit)? = null,
) {
    require(icon != null || iconResId != null) {
        "Harus isi salah satu: icon atau iconResId"
    }

    val iconSize = circleSize * 0.55f
    val assetSize = circleSize * (scale ?: 0.35f)
    val isSingleWord = !label.trim().contains(" ")

    Column(
        modifier = modifier
            .width(width)
            .clickable(enabled = onTap != null) { onTap?.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            when {
                icon != null -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(iconSize),
                    // TODO: sesuaikan dengan token AppTheme.blue2
                    tint = MaterialTheme.colorScheme.primary,
                )
                iconResId != null -> Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = label,
                    modifier = Modifier.size(assetSize),
                    // Unspecified -> pertahankan warna asli svg multi-warna.
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            textAlign = TextAlign.Center,
            style = if (useThemeStyle) {
                MaterialTheme.typography.titleSmall
            } else {
                TextStyle(
                    fontSize = 10.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W500,
                    // TODO: sesuaikan dengan token AppTheme.gray950
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            maxLines = if (isSingleWord) 1 else 2,
            overflow = if (isSingleWord) TextOverflow.Ellipsis else TextOverflow.Visible,
            softWrap = !isSingleWord,
        )
    }
}