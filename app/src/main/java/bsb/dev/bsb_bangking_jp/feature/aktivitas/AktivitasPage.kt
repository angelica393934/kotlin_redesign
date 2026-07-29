package bsb.dev.bsb_bangking_jp.pages.aktivitas

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.component.AppHeader
import bsb.dev.bsb_bangking_jp.core.component.EmptyState
import bsb.dev.bsb_bangking_jp.core.component.RekeningLainnyaSheet
import bsb.dev.bsb_bangking_jp.core.component.RekeningSheetMode
import bsb.dev.bsb_bangking_jp.core.dummy.DummyData
import bsb.dev.bsb_bangking_jp.core.dummy.DummyRekening
import bsb.dev.bsb_bangking_jp.core.dummy.DummyTransaksi
import bsb.dev.bsb_bangking_jp.pages.beranda.section.SaldoCardBase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AktivitasPage(
    rekeningList: List<DummyRekening> = DummyData.rekeningList,
    transaksiList: List<DummyTransaksi> = DummyData.transaksiList,
    onCariTransaksiClick: () -> Unit = {},
) {
    var accountNo by remember {
        mutableStateOf(
            rekeningList.firstOrNull { it.isPrimary }?.number ?: rekeningList.firstOrNull()?.number,
        )
    }
    var showRekeningSheet by remember { mutableStateOf(false) }

    val activeRekening = remember(rekeningList, accountNo) {
        rekeningList.firstOrNull { it.number == accountNo } ?: rekeningList.firstOrNull()
    }

    val groupedTransaksi = remember(transaksiList) {
        transaksiList
            .sortedByDescending { parseTanggalDummy(it.tanggal) }
            .groupBy { it.tanggal }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.background),
        ) {
            // ===== HEADER + SALDO CARD (area tinggi 200dp) =====
            // NOTE: SaldoCardSelector (swipe carousel) diganti dengan kartu rekening aktif
            // + RekeningLainnyaSheet, biar konsisten dengan pola "Rekening Lainnya" di
            // SaldoCardDashboard (beranda).
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                AppHeader(title = "", height = 160.dp)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = 65.dp),
                ) {
                    activeRekening?.let { rekening ->
                        SaldoCardBase(
                            nama = rekening.name,
                            rekening = rekening.number,
                            saldo = rekening.saldo,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Rekening Aktif",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f),
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Row(
                                    modifier = Modifier.clickable {
                                        if (rekeningList.size > 1) showRekeningSheet = true
                                    },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "Ganti Rekening",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ===== "Transaksi Bulan Ini" + "Cari Transaksi" =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 10.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.label_transaksi_bulan_ini),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Row(
                    modifier = Modifier.clickable(onClick = onCariTransaksiClick),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.label_cari_transaksi),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // TODO: padanan FilterChipBar (chip filter aktif dari ActivityHistoryBloc)
            // belum diporting -- sambungkan begitu ActivityFilterChipMapper/state
            // filter tersedia.

            // ===== LIST TRANSAKSI =====
            Box(modifier = Modifier.weight(1f)) {
                if (transaksiList.isEmpty()) {
                    EmptyState(
                        message = stringResource(R.string.msg_tidak_ada_aktivitas),
                        subMessage = stringResource(R.string.msg_belum_ada_transaksi),
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        groupedTransaksi.forEach { (tanggal, items) ->
                            item(key = "header_$tanggal") {
                                TanggalHeader(tanggal = tanggal)
                            }
                            items(
                                items = items,
                                key = { "${tanggal}_${it.detail}_${it.nominal}" },
                            ) { transaksi ->
                                TransaksiItem(transaksi = transaksi)
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

        if (showRekeningSheet && rekeningList.isNotEmpty()) {
            RekeningLainnyaSheet(
                daftarRekening = rekeningList,
                mode = RekeningSheetMode.LIHAT_REKENING_LAIN,
                rekeningAktif = accountNo,
                title = "Pilih Rekening",
                onDismiss = { showRekeningSheet = false },
                onSelected = { selected ->
                    if (accountNo != selected.number) {
                        accountNo = selected.number
                        // TODO: trigger ActivityHistoryEvent.refresh(accountNumber)
                        // saat ActivityHistoryBloc/ViewModel sudah ada.
                    }
                },
            )
        }
    }
}

@Composable
private fun TanggalHeader(tanggal: String, modifier: Modifier = Modifier) {
    // NOTE: DateFormatterUtil.fromYYMMDD belum diporting -- tanggal dummy
    // ("02 Jul 2026") sudah dalam format tampilan jadi ditampilkan apa adanya.
    Box(
        modifier = modifier
            .fillMaxWidth()
            // TODO: sesuaikan dengan token AppTheme.blue8
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Text(text = tanggal, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun TransaksiItem(transaksi: DummyTransaksi, modifier: Modifier = Modifier) {
    val warnaNominal = if (transaksi.isMasuk) {
        // TODO: sesuaikan dengan token AppTheme.green700
        Color(0xFF2E7D32)
    } else {
        // TODO: sesuaikan dengan token AppTheme.gray950
        MaterialTheme.colorScheme.onSurface
    }

    val icon: ImageVector = when (transaksi.jenis) {
        "Transfer" -> Icons.Filled.CompareArrows
        "Tagihan" -> Icons.AutoMirrored.Filled.ReceiptLong
        "Top Up" -> Icons.Filled.AccountBalanceWallet
        else -> Icons.AutoMirrored.Filled.HelpOutline
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 15.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    // TODO: sesuaikan dengan token AppTheme.blue8
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    // TODO: sesuaikan dengan token AppTheme.blue2
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(25.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = transaksi.jenis,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = transaksi.detail,
                        fontSize = 12.sp,
                        // TODO: sesuaikan dengan token AppTheme.gray400
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = transaksi.nominal,
                    style = MaterialTheme.typography.titleMedium,
                    color = warnaNominal,
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            // TODO: sesuaikan dengan token AppTheme.gray100
            color = MaterialTheme.colorScheme.surfaceVariant,
        )

    }
}

private fun parseTanggalDummy(tanggal: String): Date {
    return try {
        SimpleDateFormat("dd MMM yyyy", Locale.US).parse(tanggal) ?: Date(0)
    } catch (e: ParseException) {
        Date(0)
    }
}