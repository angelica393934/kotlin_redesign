package bsb.dev.bsb_bangking_jp.pages.beranda

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
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
                nama = DummyData.profile.nama,
                photoBytes = DummyData.profile.photoBytes,
                onNotificationClick = onNotificationClick,
                onLogoutClick = onLogoutClick,
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