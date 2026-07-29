package bsb.dev.bsb_bangking_jp.pages.pesan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.component.AppHeader
import bsb.dev.bsb_bangking_jp.core.component.EmptyState
import bsb.dev.bsb_bangking_jp.core.dummy.DummyData
import bsb.dev.bsb_bangking_jp.core.dummy.DummyPesan
import bsb.dev.bsb_bangking_jp.feature.pesan.section.PesanSectionTanggal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PesanPage(
    pesanList: List<DummyPesan> = DummyData.pesanList,
    onCariPesanClick: () -> Unit = {},
) {
    val groupedPesan = remember(pesanList) {
        pesanList
            .sortedByDescending { parseTanggalDummy(it.tanggal) }
            .groupBy { it.tanggal }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AppHeader(title = "Pesan", )

        // ===== "Semua Pesan" + "Cari Pesan" (Filter Bar - UI saja) =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.label_semua_pesan),
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.clickable(onClick = onCariPesanClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    // TODO: sesuaikan dengan token AppTheme.blue1
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(15.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.label_cari_pesan),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        // ===== LIST PESAN =====
        Box(modifier = Modifier.weight(1f)) {
            if (pesanList.isEmpty()) {
                EmptyState(
                    message = stringResource(R.string.msg_tidak_ada_pesan),
                    subMessage = stringResource(R.string.msg_belum_ada_pesan),
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    groupedPesan.forEach { (tanggal, items) ->
                        item(key = "pesan_group_$tanggal") {
                            PesanSectionTanggal(tanggal = tanggal, items = items)
                        }
                    }
                    item(key = "bottom_spacer") {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))

    }
}

/** Padanan sederhana untuk mengurutkan dummy tanggal ("02 Jul 2026") terbaru -> terlama. */
private fun parseTanggalDummy(tanggal: String): Date {
    return try {
        SimpleDateFormat("dd MMM yyyy", Locale.US).parse(tanggal) ?: Date(0)
    } catch (e: ParseException) {
        Date(0)
    }
}