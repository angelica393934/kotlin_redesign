package bsb.dev.bsb_bangking_jp.feature.pesan.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bsb.dev.bsb_bangking_jp.core.dummy.DummyPesan

/**
 * Padanan dari Pages/pesan/section/PesanItem.dart (PesanSectionTanggal + PesanItem).
 *
 * BELUM terhubung ke MessageHistoryBloc/mapper apa pun -- item pesan
 * dikirim sebagai DummyPesan yang field-nya (title/subtitle/amount/status)
 * sudah dalam bentuk siap tampil, persis seperti hasil
 * mapTitle/mapSubtitle/MoneyFormatter/mapStatus di Dart.
 */
@Composable
fun PesanSectionTanggal(
    tanggal: String,
    items: List<DummyPesan>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // TODO: sesuaikan dengan token AppTheme.blue8
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 8.dp),
        ) {
            Text(
                text = tanggal,
                style = MaterialTheme.typography.titleMedium,
                // TODO: sesuaikan dengan token AppTheme.gray300
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items.forEach { pesan ->
            PesanItemRow(pesan = pesan)
        }
    }
}

@Composable
private fun PesanItemRow(pesan: DummyPesan, modifier: Modifier = Modifier) {
    val icon = getPesanIcon(pesan.title)
    val statusColor = getStatusColor(pesan.status)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                // TODO: sesuaikan dengan token AppTheme.blue2
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = pesan.title, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pesan.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    // TODO: sesuaikan dengan token AppTheme.gray400
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = pesan.amount,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = pesan.status,
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 12.sp,
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            // TODO: sesuaikan dengan token AppTheme.gray100
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 1.dp,
        )
    }
}

/** Padanan dari _getIcon() di Dart -- deteksi ikon otomatis berdasarkan title. */
private fun getPesanIcon(title: String): ImageVector {
    val t = title.lowercase()
    return when {
        t.contains("transfer") -> Icons.Filled.CompareArrows
        t.contains("top up") -> Icons.Filled.AccountBalanceWallet
        t.contains("qris") -> Icons.Filled.QrCode
        t.contains("tagihan") -> Icons.AutoMirrored.Filled.ReceiptLong
        else -> Icons.Filled.Notifications
    }
}

/** Padanan dari _getStatusColor() di Dart -- warna badge status otomatis. */
@Composable
private fun getStatusColor(status: String): Color = when (status.lowercase()) {
    "berhasil" -> Color(0xFF4CAF50) // padanan Colors.green
    "gagal" -> Color(0xFFF44336) // padanan Colors.red
    // TODO: sesuaikan dengan token AppTheme.gray400
    "pending" -> MaterialTheme.colorScheme.outline
    // TODO: sesuaikan dengan token AppTheme.gray300
    else -> MaterialTheme.colorScheme.onSurfaceVariant
}