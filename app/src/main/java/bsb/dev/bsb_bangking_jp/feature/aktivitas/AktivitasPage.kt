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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bsb.dev.bsb_bangking_jp.core.component.AppHeader
import bsb.dev.bsb_bangking_jp.core.component.EmptyState
import bsb.dev.bsb_bangking_jp.core.component.FilterChipBar
import bsb.dev.bsb_bangking_jp.core.component.SaldoCardEmpty
import bsb.dev.bsb_bangking_jp.core.components.skeleton.SkeletonList
import bsb.dev.bsb_bangking_jp.core.components.skeleton.SkeletonSaldoCard
import bsb.dev.bsb_bangking_jp.core.util.ActivityFilterChipMapper
import bsb.dev.bsb_bangking_jp.core.util.CurrencyUtils
import bsb.dev.bsb_bangking_jp.core.util.DateFormatterUtil
import bsb.dev.bsb_bangking_jp.core.util.groupByDateSortedDesc
import bsb.dev.bsb_bangking_jp.feature.aktivitas.component.FilterTransaksiModal
import bsb.dev.bsb_bangking_jp.feature.aktivitas.data.HistoryItem
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityFilterPayload
import bsb.dev.bsb_bangking_jp.feature.aktivitas.presentation.ActivityHistoryViewModel
import bsb.dev.bsb_bangking_jp.feature.aktivitas.section.SaldoCardSelector
import bsb.dev.bsb_bangking_jp.feature.beranda.presentation.BerandaViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AktivitasPage(
    berandaViewModel: BerandaViewModel = koinInject(),
    activityViewModel: ActivityHistoryViewModel = koinViewModel(),
) {
    val berandaState by berandaViewModel.uiState.collectAsStateWithLifecycle()
    val activityState by activityViewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    var accountNo by remember { mutableStateOf<String?>(null) }
    var showFilterModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (berandaState.rekeningList == null) berandaViewModel.loadRekeningLainnya()
    }

    LaunchedEffect(berandaState.rekeningList) {
        val list = berandaState.rekeningList
        if (accountNo == null && !list.isNullOrEmpty()) {
            val primary = list.firstOrNull { it.isPrimary } ?: list.first()
            accountNo = primary.number
            activityViewModel.getInitial(primary.number)
        }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = layoutInfo.totalItemsCount
            totalItems > 0 && lastVisible >= totalItems - 3
        }
    }
    LaunchedEffect(shouldLoadMore, activityState.hasMore, activityState.isLoadMore, activityState.isLoading) {
        if (shouldLoadMore && activityState.hasMore && !activityState.isLoadMore && !activityState.isLoading) {
            accountNo?.let { activityViewModel.loadMore(it) }
        }
    }

    val grouped = remember(activityState.items) { groupByDateSortedDesc(activityState.items) }
    val chips = remember(activityState.activeFilter) {
        ActivityFilterChipMapper.fromPayload(activityState.activeFilter)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // ===== HEADER + SALDO CARD =====
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            AppHeader(title = "", showBackButton = false, height = 160.dp)
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = 65.dp),
            ) {
                when {
                    berandaState.isRekeningLoading && berandaState.rekeningList == null -> {
                        SkeletonSaldoCard()
                    }
                    berandaState.rekeningList != null -> {
                        SaldoCardSelector(
                            rekeningList = berandaState.rekeningList!!,
                            activeAccountNumber = accountNo,
                            onRekeningSelected = { selected ->
                                if (accountNo != selected.number) {
                                    accountNo = selected.number
                                    activityViewModel.getInitial(selected.number)
                                }
                            },
                        )
                    }
                    else -> {
                        SaldoCardEmpty(
                            onRetry = { berandaViewModel.loadRekeningLainnya(forceRefresh = true) },
                        )
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
                text = "Transaksi Bulan Ini",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )
            Row(
                modifier = Modifier.clickable { showFilterModal = true },
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
                    text = "Cari Transaksi",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        // ===== FILTER CHIP BAR =====
        FilterChipBar(
            items = chips,
            onClearAll = if (chips.isEmpty()) null else {
                { accountNo?.let { activityViewModel.getInitial(it) } }
            },
            onRemove = { chip ->
                val current = activityState.activeFilter ?: return@FilterChipBar
                val updated = ActivityFilterChipMapper.removeChip(current, chip.key)
                accountNo?.let { activityViewModel.applyFilter(it, updated) }
            },
        )

        // ===== LIST TRANSAKSI =====
        Box(modifier = Modifier.weight(1f)) {
            when {
                activityState.isLoading && activityState.items.isEmpty() -> {
                    SkeletonList()
                }
                activityState.error != null && activityState.items.isEmpty() -> {
                    EmptyState(
                        modifier = Modifier.fillMaxSize(),
                        message = "Data aktivitas tidak dapat dimuat.",
                        subMessage = "Terjadi kesalahan saat mengambil data.\nPeriksa koneksi anda dan coba lagi.",
                        actionText = "Coba Lagi",
                        onAction = { accountNo?.let { activityViewModel.getInitial(it) } },
                    )
                }
                activityState.items.isEmpty() -> {
                    EmptyState(
                        modifier = Modifier.fillMaxSize(),
                        message = "Tidak ada aktivitas transaksi.",
                        subMessage = "Belum ditemukan catatan transaksi pada periode ini.",
                        actionText = null,
                    )
                }
                else -> {
                    PullToRefreshBox(
                        isRefreshing = activityState.isLoading,
                        onRefresh = { accountNo?.let { activityViewModel.refresh(it) } },
                        state = pullToRefreshState,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                            grouped.forEach { (tanggal, itemsForDate) ->
                                item(key = "header_$tanggal") {
                                    TanggalHeader(tanggal = DateFormatterUtil.fromYYMMDD(tanggal))
                                }
                                itemsIndexed(
                                    items = itemsForDate,
                                    key = { index, it -> it.transactionId.ifEmpty { "$tanggal-$index" } },
                                ) { _, transaksi ->
                                    TransaksiItem(transaksi = transaksi)
                                }
                            }
                            if (activityState.isLoadMore) {
                                item(key = "load_more") {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(28.dp))
                                    }
                                }
                            }
                            item(key = "bottom_spacer") {
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }

    if (showFilterModal) {
        FilterTransaksiModal(
            currentFilter = activityState.activeFilter ?: ActivityFilterPayload.initial(),
            onDismiss = { showFilterModal = false },
            onApply = { updated -> accountNo?.let { activityViewModel.applyFilter(it, updated) } },
        )
    }
}

@Composable
private fun TanggalHeader(tanggal: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Text(text = tanggal, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun TransaksiItem(transaksi: HistoryItem, modifier: Modifier = Modifier) {
    val warnaNominal = if (transaksi.isMasuk) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface

    val jenisLower = transaksi.jenisTransaksi.lowercase()
    val icon: ImageVector = when {
        "transfer" in jenisLower -> Icons.Filled.CompareArrows
        "tagihan" in jenisLower -> Icons.AutoMirrored.Filled.ReceiptLong
        "top" in jenisLower -> Icons.Filled.AccountBalanceWallet
        else -> Icons.AutoMirrored.Filled.HelpOutline
    }

    // deskripsiTransaksi = "Jenis arah\nrekeningTujuan" -- baris 1 jadi title, baris 2 jadi subtitle
    val descLines = transaksi.deskripsiTransaksi.split("\n")
    val title = descLines.getOrNull(0)?.takeIf { it.isNotBlank() }
        ?: transaksi.jenisTransaksi.ifBlank { "Transaksi" }
    val subtitle = descLines.getOrNull(1).orEmpty()

    val nominalInt = transaksi.amount.toDoubleOrNull()?.toInt() ?: 0
    val nominalFormatted = CurrencyUtils.formatRupiah(nominalInt)
    val nominalDisplay = if (transaksi.isMasuk) "+ $nominalFormatted" else "- $nominalFormatted"

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
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
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
                    Text(text = title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                    if (subtitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = nominalDisplay,
                    style = MaterialTheme.typography.titleMedium,
                    color = warnaNominal,
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}