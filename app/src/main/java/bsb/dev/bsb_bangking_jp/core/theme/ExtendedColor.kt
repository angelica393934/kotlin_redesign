package bsb.dev.bsb_bangking_jp.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    // Teks
    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,

    // Divider / border / stroke
    val divider: Color,
    val strip: Color, // strip di intro

    // Status
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val danger: Color,
    val onDanger: Color,

    // Container tambahan
    val cardBackground: Color,
    val inputBackground: Color,
)

val LightExtendedColors = ExtendedColors(
    textPrimary = Gray950,
    textSecondary = Gray500,
    textDisabled = Gray200,

    divider = Gray300,
    strip = Gray100,

    success = Green500,
    onSuccess = White,
    warning = Orange500,
    onWarning = White,
    danger = Red500,
    onDanger = White,

    cardBackground = Gray400,
    inputBackground = Gray50,
)

val DarkExtendedColors = ExtendedColors(
    textPrimary = White,
    textSecondary = Gray300,
    textDisabled = Gray500,

    divider = Gray600,
    strip = Gray800,

    success = Green400,
    onSuccess = Gray950,
    warning = Orange400,
    onWarning = Gray950,
    danger = Red400,
    onDanger = Gray950,

    cardBackground = Gray900,
    inputBackground = Gray800,
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }