package bsb.dev.bsb_bangking_jp.shared.logout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.component.AppButton
import bsb.dev.bsb_bangking_jp.core.component.AppModalBottomSheet
import bsb.dev.bsb_bangking_jp.core.component.AppModalConfirm
import bsb.dev.bsb_bangking_jp.core.component.LocalLoadingOverlay
import bsb.dev.bsb_bangking_jp.core.component.LocalToastState
import bsb.dev.bsb_bangking_jp.shared.logout.presentation.LogoutUiState
import bsb.dev.bsb_bangking_jp.shared.logout.presentation.LogoutViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Padanan LogoutConfirmSheet.dart -- bottom sheet konfirmasi keluar aplikasi.
 * Dipanggil dari mana pun (mis. HaloUserSection.onLogoutClick di Beranda, atau
 * menu "Keluar" di PengaturanPage).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutConfirmSheet(
    onDismiss: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: LogoutViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val loadingOverlay = LocalLoadingOverlay.current
    val toastState = LocalToastState.current

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LogoutUiState.Loading -> loadingOverlay.show()
            is LogoutUiState.Success -> {
                loadingOverlay.hide()
                onLoggedOut()
            }
            is LogoutUiState.Failure -> {
                loadingOverlay.hide()
                toastState.showError(state.respMessage)
            }
            is LogoutUiState.Initial -> loadingOverlay.hide()
        }
    }

    AppModalConfirm(
        onDismissRequest = onDismiss,
        imageRes = R.drawable.logout,
        title = "Apakah Anda yakin ingin keluar dari aplikasi?",
        cancelText = "Batal",
        onCancel = onDismiss,
        confirmText = "Keluar",
        onConfirm = { viewModel.logout() },
    )
}