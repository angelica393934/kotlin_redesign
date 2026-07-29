package bsb.dev.bsb_bangking_jp.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.core.theme.extendedColors

/**
 * Padanan `_buildDetailRow` yang berulang di beberapa halaman Flutter (PeriksaKembaliSheet,
 * TransferBerhasilPage, TransferBerhasilDijadwalkanPage) -- disatukan di sini supaya tidak ada
 * 3 copy fungsi yang isinya sama.
 *
 * Auto hijau kalau `value` == "gratis" (case-insensitive), sama seperti versi Flutter aslinya.
 */
@Composable
fun TransactionDetailRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle? = null,
    valueStyle: TextStyle? = null,
) {
    val isGratis = value.trim().equals("gratis", ignoreCase = true)

    val finalValueStyle = valueStyle ?: if (isGratis) {
        MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.extendedColors.success)
    } else {
        MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.extendedColors.textSecondary,
            fontWeight = FontWeight.Medium,
        )
    }
    val finalTitleStyle = titleStyle ?: MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.extendedColors.textSecondary,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = title, style = finalTitleStyle)
        Text(
            text = value,
            style = finalValueStyle,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

/** Padanan `formatRupiah` yang muncul berulang di beberapa file Flutter. */
fun formatRupiah(amount: Int): String {
    val formatted = "%,d".format(amount).replace(",", ".")
    return "Rp $formatted"
}

/** Menutupi bagian tengah nomor rekening dengan "*", mis. "12****90". */
fun maskAccountNumber(number: String): String {
    if (number.length <= 4) return number
    val visibleStart = number.take(2)
    val visibleEnd = number.takeLast(2)
    return "$visibleStart${"*".repeat(number.length - 4)}$visibleEnd"
}