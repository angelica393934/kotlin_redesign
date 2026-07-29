package bsb.dev.bsb_bangking_jp.core.util

import android.graphics.Paint
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.bottomShadow(
    color: Color,
    blur: Dp = 6.dp,
    offsetY: Dp = 2.dp,
): Modifier = this.drawBehind {

    val paint = Paint().apply {
        this.color = android.graphics.Color.TRANSPARENT
        setShadowLayer(
            blur.toPx(),
            0f,
            offsetY.toPx(),
            color.toArgb()
        )
    }

    drawContext.canvas.nativeCanvas.drawRect(
        0f,
        0f,
        size.width,
        size.height,
        paint
    )
}