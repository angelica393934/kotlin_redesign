package bsb.dev.bsb_bangking_jp.feature.transfer.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.component.AppButton
import bsb.dev.bsb_bangking_jp.core.component.RekeningLainnyaSheet
import bsb.dev.bsb_bangking_jp.core.component.RekeningSheetMode
import bsb.dev.bsb_bangking_jp.core.dummy.DummyRekening
import bsb.dev.bsb_bangking_jp.core.theme.extendedColors

sealed interface RekeningSumberUiState {
    data object Loading : RekeningSumberUiState
    data class Success(val data: List<DummyRekening>) : RekeningSumberUiState
    data class Error(val message: String) : RekeningSumberUiState
}

@Composable
fun RekeningSumberCard(
    state: RekeningSumberUiState,
    activeAccountNumber: String?,
    onAccountChanged: (DummyRekening) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "Rekening Sumber", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        when (state) {
            is RekeningSumberUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    // TODO: ganti dengan SkeletonRekeningCard (shimmer) begitu padanan
                    // widgets/skeletons/section/skeleton_rekening_card.dart dikonversi.
                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                }
            }

            is RekeningSumberUiState.Error -> {
                RekeningErrorCard(message = state.message, onRetry = onRetry)
            }

            is RekeningSumberUiState.Success -> {
                RekeningSumberContent(
                    daftarRekening = state.data,
                    activeAccountNumber = activeAccountNumber,
                    onAccountChanged = onAccountChanged,
                )
            }
        }
    }
}

@Composable
private fun RekeningSumberContent(
    daftarRekening: List<DummyRekening>,
    activeAccountNumber: String?,
    onAccountChanged: (DummyRekening) -> Unit,
) {
    if (daftarRekening.isEmpty()) return

    var showSheet by remember { mutableStateOf(false) }

    val aktif = daftarRekening.firstOrNull { it.number == activeAccountNumber }
        ?: daftarRekening.firstOrNull { it.isPrimary }
        ?: daftarRekening.first()

    // Padanan WidgetsBinding.instance.addPostFrameCallback di Flutter: beri tahu parent
    // rekening aktif default begitu tersedia / berubah.
    LaunchedEffect(aktif.number) {
        if (activeAccountNumber != aktif.number) onAccountChanged(aktif)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, MaterialTheme.extendedColors.textDisabled, RoundedCornerShape(25.dp)),
    ) {
        // Watermark logo transparan, padanan Positioned.fill + Transform.scale(4) di Flutter.
        Image(
            painter = painterResource(id = R.drawable.bg_card),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                // NOTE: field "classification" belum ada di DummyRekening -- lihat catatan
                // yang sama di RekeningLainnyaSheet.kt.
                Text(text = "Tabungan Sekarang", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = aktif.saldo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = aktif.number,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.extendedColors.textSecondary,
                )
            }

            AppButton(
                text = "Ubah",
                onClick = { showSheet = true },
                backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                textColor= MaterialTheme.colorScheme.inverseOnSurface,
                smallWidth = true,
                textStyle = MaterialTheme.typography.titleSmall
            )
        }
    }

    if (showSheet) {
        RekeningLainnyaSheet(
            daftarRekening = daftarRekening,
            mode = RekeningSheetMode.REKENING_SUMBER,
            rekeningAktif = aktif.number,
            title = "Pilih Rekening Sumber",
            onDismiss = { showSheet = false },
            onSelected = { selected -> onAccountChanged(selected) },
        )
    }
}