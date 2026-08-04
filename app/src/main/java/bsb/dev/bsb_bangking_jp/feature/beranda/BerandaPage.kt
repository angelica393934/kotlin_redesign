
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bsb.dev.bsb_bangking_jp.feature.beranda.section.MenuUtama
import bsb.dev.bsb_bangking_jp.core.dummy.DummyData
import bsb.dev.bsb_bangking_jp.core.component.BannerKeamanan
import bsb.dev.bsb_bangking_jp.feature.beranda.section.HaloUserSection
import bsb.dev.bsb_bangking_jp.feature.beranda.section.SaldoCardDashboard
import bsb.dev.bsb_bangking_jp.pages.beranda.section.BeritaSection
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.feature.beranda.viewmodel.BerandaViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaPage(
    navController: NavController,
    onNotificationClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onLihatSemuaBeritaClick: () -> Unit = {},
    onTransferClick: () -> Unit = {},
    onTopUpClick: () -> Unit = {},
    onVirtualAccountClick: () -> Unit = {},
    onBsbCashClick: () -> Unit = {},
    onPajakPendidikanClick: () -> Unit = {},
    onTagihanClick: () -> Unit = {},
    onCardlessClick: () -> Unit = {},
    onLainnyaClick: () -> Unit = {},
    BerandaViewModel: BerandaViewModel = koinViewModel(),
) {
    // 🔹 Profil dari SessionManager (hasil login sukses), reaktif lewat StateFlow
    // supaya otomatis update begitu pull-to-refresh berhasil. Null kalau entah
    // kenapa belum ada sesi -- fallback ke DummyData supaya UI tidak crash.
    val profile by BerandaViewModel.profile.collectAsStateWithLifecycle()
    val isRefreshing by BerandaViewModel.isRefreshing.collectAsStateWithLifecycle()

    val namaUser = profile?.let { "${it.firstName} ${it.lastName}".trim() }
        ?.takeIf { it.isNotBlank() }
        ?: DummyData.profile.nama

    // 🔹 Pull-to-refresh: gulir ke bawah di halaman ini akan hit GET /auth/me.
    // Kalau berhasil, `profile` di atas otomatis ter-update (lihat
    // BerandaViewModel.refresh()). Kalau gagal, tidak terjadi apa-apa --
    // data yang sudah tampil tetap dipertahankan.
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { BerandaViewModel.refresh() },
        modifier = Modifier.fillMaxSize(),
    ) {
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
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 60.dp,),
            ) {
                HaloUserSection(
                    nama = namaUser,
                    photoUrl = profile?.image?.takeIf { it.isNotBlank() },
                    photoBytes = null,
                    onNotificationClick = onNotificationClick,
                    onLogoutClick = {
                        BerandaViewModel.logout()
                        onLogoutClick()
                    },
                )

                Spacer(modifier = Modifier.height(15.dp))

                DummyData.bannerList.firstOrNull()?.let { banner ->
                    BannerKeamanan(desc = banner.message)
                }

                Spacer(modifier = Modifier.height(15.dp))

                SaldoCardDashboard(rekeningList = DummyData.rekeningList)

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

                BeritaSection(
                    berita = DummyData.beritaList,
                    onLihatSemuaClick = onLihatSemuaBeritaClick,
                    navController = navController,
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
