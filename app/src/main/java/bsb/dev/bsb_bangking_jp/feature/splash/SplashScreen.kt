package bsb.dev.bsb_bangking_jp.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.util.getAppVersion

@Composable
fun SplashScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val appVersion = remember {
        getAppVersion(context)
    }

    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("intro") {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logosplash),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "PT Bank Pembangunan Daerah Sumatera Selatan dan Bangka Belitung berizin dan diawasi oleh Otoritas Jasa Keuangan (OJK) dan Bank Indonesia (BI), serta merupakan peserta penjaminan Lembaga Penjamin Simpanan (LPS).",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Bank Sumsel Babel Mobile Banking\nVersi $appVersion",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}