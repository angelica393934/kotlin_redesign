package bsb.dev.bsb_bangking_jp.feature.portal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bsb.dev.bsb_bangking_jp.core.component.AppButton
import bsb.dev.bsb_bangking_jp.core.component.AppTextField
import bsb.dev.bsb_bangking_jp.core.component.LocalToastState
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.res.stringResource
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.components.AppCheckBox
import bsb.dev.bsb_bangking_jp.core.components.AppMenu
import bsb.dev.bsb_bangking_jp.core.theme.extendedColors
import bsb.dev.bsb_bangking_jp.feature.portal.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginSheet(
    navController: NavController,
    onDismiss: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel(),
) {
    // ToastState global, sama seperti yang sudah dipakai di halaman lain
    // (AppToast.kt + LocalToastState + ToastHost yang di-provide di AppNavigation).
    val toastState = LocalToastState.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val loginTitle = stringResource(R.string.login_title)
    val userIdLabel = stringResource(R.string.user_id_label)
    val userIdHint = stringResource(R.string.user_id_hint)
    val passwordLabel = stringResource(R.string.password_label)
    val passwordHint = stringResource(R.string.password_hint)
    val rememberUserId = stringResource(R.string.remember_user_id)
    val login = stringResource(R.string.login_button)
    val forgotPassword = stringResource(R.string.forgot_userid_password)
    val activation = stringResource(R.string.menu_activation)
    val registration = stringResource(R.string.menu_registration)
    val atmLocation = stringResource(R.string.menu_atm_location)

    // TODO: sementara pakai kredensial dummyjson untuk testing:
    // username = "emilys", password = "emilyspass"
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    // Navigasi ke navbar begitu login sukses (hasil hit API).
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            viewModel.onNavigated()
            navController.navigate("navbar") {
                popUpTo("portal") { inclusive = true }
            }
        }
    }

    // Tampilkan error dari API (mis. "Invalid credentials" / tidak ada koneksi)
    // pakai AppToast bawaan, bukan Toast.makeText default Android lagi.
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            toastState.showError(message)
            viewModel.clearError()
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = loginTitle,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Tombol close (X), pojok kanan atas
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup",
                    tint = MaterialTheme.extendedColors.textPrimary,
                )
            }
        }

        AppTextField(
            value = userId,
            onValueChange = { userId = it },
            labelText = userIdLabel,
            hintText = userIdHint,
            icon = Icons.Default.Person,
            readOnly = uiState.isLoading,
        )
        Spacer(modifier = Modifier.height(10.dp))

        AppTextField(
            value = password,
            onValueChange = { password = it },
            labelText = passwordLabel,
            hintText = passwordHint,
            icon = Icons.Default.Lock,
            obscureText = true,
            readOnly = uiState.isLoading,
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.toggleable(
                value = rememberMe,
                onValueChange = { rememberMe = it }
            )
        ) {
            AppCheckBox(
                modifier = Modifier.padding(start = 16.dp, end = 12.dp),
                value = rememberMe,
                onChanged = { rememberMe = it },
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.extendedColors.cardBackground,
                text = rememberUserId
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        AppButton(
            text = if (uiState.isLoading) "Memproses..." else login,
            enabled = !uiState.isLoading,
            onClick = {
                // Hit API POST /auth/login lewat ViewModel -> LoginUseCase -> Repository.
                viewModel.login(userId, password)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = {
                // Bottom sheet lupa akun
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                forgotPassword,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.extendedColors.divider,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {

            AppMenu(
                icon = Icons.Outlined.Security,
                label = activation
            )

            AppMenu(
                icon = Icons.Outlined.PersonAdd,
                label = registration,
            )

            AppMenu(
                icon = Icons.Outlined.LocationOn,
                label = atmLocation,
                onTap = {
                    navController.navigate("lokasiatm")
                }
            )
        }
    }
}
