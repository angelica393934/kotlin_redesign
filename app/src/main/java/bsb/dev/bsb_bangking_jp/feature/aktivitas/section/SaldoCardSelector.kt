package bsb.dev.bsb_bangking_jp.feature.aktivitas.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.dummy.DummyRekening
import bsb.dev.bsb_bangking_jp.pages.beranda.section.SaldoCardBase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaldoCardSelector(
    rekeningList: List<DummyRekening>,
    activeAccountNumber: String?,
    onRekeningSelected: (DummyRekening) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showSheet by remember { mutableStateOf(false) }

    if (rekeningList.isEmpty()) return

    val rekeningAktif = rekeningList.firstOrNull { it.number == activeAccountNumber }
        ?: rekeningList.firstOrNull { it.isPrimary }
        ?: rekeningList.first()

    SaldoCardBase(
        nama = rekeningAktif.name,
        rekening = rekeningAktif.number,
        saldo = rekeningAktif.saldo,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.label_pilih_rekening),
                style = MaterialTheme.typography.titleMedium,
                // TODO: sesuaikan dengan token AppTheme.gray300
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier.clickable { showSheet = true },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.label_rekening_lainnya),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }

    if (showSheet) {
        // TODO: ganti dengan padanan RekeningLainnyaSheet
        // (mode = RekeningSheetMode.lihatRekeningLain) kalau butuh tampilan
        // yang lebih kaya (badge klasifikasi rekening, dst). Versi ini list
        // sederhana dulu supaya alurnya tetap bisa dicoba dgn data dummy.
        // Catatan: field `rek.visible` di Dart belum ada padanannya di
        // DummyRekening -- tambahkan kalau perlu memfilter rekening tersembunyi.
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                rekeningList.forEach { rekening ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRekeningSelected(rekening)
                                showSheet = false
                            }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(text = rekening.name, style = MaterialTheme.typography.bodyMedium)
                            Text(text = rekening.number, style = MaterialTheme.typography.bodySmall)
                        }
                        Text(text = rekening.saldo, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}