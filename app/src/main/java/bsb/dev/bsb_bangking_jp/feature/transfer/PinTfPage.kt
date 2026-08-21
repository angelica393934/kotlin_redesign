package bsb.dev.bsb_bangking_jp.feature.transfer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bsb.dev.bsb_bangking_jp.core.component.InputPinPage
import bsb.dev.bsb_bangking_jp.core.component.LocalLoadingOverlay
import bsb.dev.bsb_bangking_jp.core.dummy.ConfirmTransferResult
import bsb.dev.bsb_bangking_jp.feature.transfer.component.PeriksaKembaliData
import bsb.dev.bsb_bangking_jp.feature.transfer.presentation.TransferNavEvent
import bsb.dev.bsb_bangking_jp.feature.transfer.presentation.TransferViewModel
import org.koin.compose.koinInject

@Composable
fun PinTfPage(
    data: PeriksaKembaliData,
    onBack: () -> Unit,
    onBerhasilSegera: (ConfirmTransferResult) -> Unit,
    onBerhasilDijadwalkan: (ConfirmTransferResult) -> Unit,
    viewModel: TransferViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val loadingOverlay = LocalLoadingOverlay.current

    // 🔹 Loading overlay mengikuti proses confirmTransfer (bukan transfer biasa).
    LaunchedEffect(uiState.isConfirming) {
        if (uiState.isConfirming) loadingOverlay.show() else loadingOverlay.hide()
    }

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            if (event is TransferNavEvent.ConfirmSuccess) {
                val result = event.result
                if (result.scheduleType == "SCHEDULED") {
                    onBerhasilDijadwalkan(result)
                } else {
                    onBerhasilSegera(result)
                }
            }
        }
    }

    InputPinPage(
        title = "Masukkan M-PIN",
        onBackClick = onBack,
        centerTitleWithBackButton = true,
        validator = null,
        externalError = uiState.confirmError,
        onPinComplete = { pin ->
            viewModel.confirmTransfer(pin) // 🔥 HIT ENDPOINT 4
        },
    )
}