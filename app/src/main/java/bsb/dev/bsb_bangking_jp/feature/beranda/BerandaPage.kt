package bsb.dev.bsb_bangking_jp.feature.beranda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.component.BannerKeamanan
import bsb.dev.bsb_bangking_jp.core.component.CustomRefreshIndicator
import bsb.dev.bsb_bangking_jp.core.component.LocalToastState
import bsb.dev.bsb_bangking_jp.core.component.SaldoCardEmpty
import bsb.dev.bsb_bangking_jp.feature.beranda.presentation.BerandaViewModel
import bsb.dev.bsb_bangking_jp.feature.beranda.section.HaloUserSection
import bsb.dev.bsb_bangking_jp.feature.beranda.section.MenuUtama
import bsb.dev.bsb_bangking_jp.feature.beranda.section.SaldoCardDashboard
import kotlin.math.roundToInt
import bsb.dev.bsb_bangking_jp.core.skeleton.HaloUserSkeleton
import bsb.dev.bsb_bangking_jp.core.skeleton.SkeletonBerita
import bsb.dev.bsb_bangking_jp.core.skeleton.SkeletonSaldoCard
import bsb.dev.bsb_bangking_jp.feature.news.presentation.NewsUiState
import bsb.dev.bsb_bangking_jp.feature.news.presentation.NewsViewModel
import bsb.dev.bsb_bangking_jp.pages.beranda.section.BeritaSection
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

private val PULL_REFRESH_MAX_PUSH = 64.dp // 🔹 seberapa jauh konten terdorong turun saat full refresh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaPage(
    navController: NavController,
    onNotificationClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onTransferClick: () -> Unit = {},
    onTopUpClick: () -> Unit = {},
    onVirtualAccountClick: () -> Unit = {},
    onBsbCashClick: () -> Unit = {},
    onPajakPendidikanClick: () -> Unit = {},
    onTagihanClick: () -> Unit = {},
    onCardlessClick: () -> Unit = {},
    onLainnyaClick: () -> Unit = {},
    berandaViewModel: BerandaViewModel = koinInject(),
) {
    val uiState by berandaViewModel.uiState.collectAsStateWithLifecycle()
    val toastState = LocalToastState.current
    val pullToRefreshState = rememberPullToRefreshState()
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        berandaViewModel.loadRekeningLainnya()
        berandaViewModel.loadBanner()
    }

    LaunchedEffect(uiState.bannerError) {
        uiState.bannerError?.let { toastState.showError(it) }
    }
    LaunchedEffect(uiState.profileError) {
        uiState.profileError?.let { toastState.showError(it) }
    }
    LaunchedEffect(uiState.rekeningError) {
        uiState.rekeningError?.let { toastState.showError(it) }
    }

    val namaUser = uiState.profile?.user?.customerName?.takeIf { it.isNotBlank() }
        ?: uiState.profile?.external?.data?.name?.takeIf { it.isNotBlank() }
        ?: "-"

    val isRefreshing = uiState.isProfileLoading || uiState.isRekeningRefreshing

    val maxPushPx = with(density) { PULL_REFRESH_MAX_PUSH.toPx() }
    val pushOffsetPx = if (isRefreshing) {
        maxPushPx
    } else {
        (pullToRefreshState.distanceFraction.coerceIn(0f, 1f) * maxPushPx)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .background(MaterialTheme.colorScheme.primary),
        )
        Image(
            painter = painterResource(id = R.drawable.bgfull),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 60.dp),
        ) {
            if (uiState.isProfileLoading) {
                HaloUserSkeleton()
            } else {
                HaloUserSection(
                    nama = namaUser, // sudah fallback "-" dari elvis chain yang ada
                    photoBytes = null,
                    onNotificationClick = onNotificationClick,
                    onLogoutClick = {
                        berandaViewModel.logout()
                        onLogoutClick()
                    },
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { berandaViewModel.refreshAll() },
                state = pullToRefreshState,
                indicator = {
                    CustomRefreshIndicator(
                        state = pullToRefreshState,
                        isRefreshing = isRefreshing,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        // 🔹 KONTEN IKUT TERDORONG TURUN sesuai jarak tarikan / status refreshing
                        .offset {
                            IntOffset(x = 0, y = pushOffsetPx.roundToInt())
                        }
                        .verticalScroll(rememberScrollState()),
                ) {
                    uiState.bannerList?.let { banners ->
                        BannerKeamanan(banners = banners) // persistKey default "beranda_pemberitahuan"
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    if (uiState.isRekeningLoading && uiState.rekeningList == null) {
                        // Loading pertama kali, belum ada data sama sekali -> skeleton
                        SkeletonSaldoCard()
                    } else {
                        // Sudah pernah ada data -> tetap tampilkan data lama walau sedang refresh
                        // (uiState.isRekeningRefreshing true tidak mengubah kondisi ini)
                        when {
                            uiState.isRekeningLoading && uiState.rekeningList == null -> {
                                // Loading pertama kali, belum ada data sama sekali -> skeleton
                                SkeletonSaldoCard()
                            }
                            uiState.rekeningList != null -> {
                                // Sudah pernah ada data -> tetap tampilkan data lama walau sedang refresh
                                // (uiState.isRekeningRefreshing true tidak mengubah kondisi ini)
                                SaldoCardDashboard(
                                    rekeningList = uiState.rekeningList!!,
                                    onSelectPrimaryAccount = { accountNumber ->
                                        berandaViewModel.setPrimaryAccount(accountNumber)
                                    },
                                )
                            }
                            else -> {
                                // Gagal & belum pernah ada data -> card tetap ada, isinya "-"
                                SaldoCardEmpty(
                                    onRetry = { berandaViewModel.loadRekeningLainnya(forceRefresh = true) },
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    MenuUtama(
                        onTransferClick = onTransferClick,
                        onTopUpClick = onTopUpClick,
                        onVirtualAccountClick = onVirtualAccountClick,
                        onBsbCashClick = onBsbCashClick,
                        onPajakPendidikanClick = onPajakPendidikanClick,
                        onTagihanClick = onTagihanClick,
                        onCardlessClick = onCardlessClick,
                        onLainnyaClick = onLainnyaClick,
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    BeritaSection(navController = navController)

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}