package bsb.dev.bsb_bangking_jp.feature.transfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.core.component.AppButton
import bsb.dev.bsb_bangking_jp.core.component.AppHeader
import bsb.dev.bsb_bangking_jp.core.component.AppModalBottomSheet
import bsb.dev.bsb_bangking_jp.core.component.AppTextField
import bsb.dev.bsb_bangking_jp.core.dummy.DummyData
import bsb.dev.bsb_bangking_jp.core.dummy.DummyTransferInquiry
import bsb.dev.bsb_bangking_jp.feature.transfer.component.PilihBankSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferBaruPage(
    onBackClick: () -> Unit = {},
    onContinueToNextPage: (DummyTransferInquiry) -> Unit = {},
) {
    var selectedBankCode by remember { mutableStateOf<String?>(null) }
    var selectedBankName by remember { mutableStateOf<String?>(null) }
    var rekeningValue by remember { mutableStateOf("") }

    var bankError by remember { mutableStateOf<String?>(null) }
    var rekeningError by remember { mutableStateOf<String?>(null) }

    var showBankSheet by remember { mutableStateOf(false) }
    var detailInquiry by remember { mutableStateOf<DummyTransferInquiry?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val bankSheetState = rememberModalBottomSheetState()

    val detailSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true, // langsung full sesuai tinggi konten, tidak ada state "setengah"
    )

    fun closeBankSheet() {
        coroutineScope.launch {
            bankSheetState.hide()
        }.invokeOnCompletion {
            if (!bankSheetState.isVisible) {
                showBankSheet = false
            }
        }
    }

    fun closeDetailSheet(onClosed: () -> Unit = {}) {
        coroutineScope.launch {
            detailSheetState.hide()
        }.invokeOnCompletion {
            if (!detailSheetState.isVisible) {
                detailInquiry = null
                onClosed()
            }
        }
    }

    fun validateAndContinue() {
        bankError = null
        rekeningError = null

        if (selectedBankCode == null) {
            bankError = "Silakan pilih bank tujuan"
        }
        if (rekeningValue.isEmpty()) {
            rekeningError = "Nomor rekening tidak boleh kosong"
        }

        if (bankError != null || rekeningError != null) return

        // TODO: ganti dengan panggilan use case/ViewModel (padanan TransferEvent.getAccountDest)
        detailInquiry = DummyData.getDummyInquiry(
            bankCode = selectedBankCode!!,
            bankName = selectedBankName!!,
            accountNumber = rekeningValue,
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppHeader(
                title = "Transfer Penerima Baru",
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 10.dp,
                    bottom = 10.dp,
                ),
            ) {
                AppButton(
                    text = "Lanjutkan",
                    icon = Icons.Default.ArrowForward,
                    onClick = { validateAndContinue() },
                )
            }
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
                AppTextField(
                    value = selectedBankName ?: "",
                    onValueChange = {},
                    labelText = "Pilih Bank Tujuan",
                    hintText = "Bank Tujuan",
                    readOnly = true,
                    isDropdown = true,
                    errorText = bankError,
                    showError = bankError != null,
                    onClick = { showBankSheet = true },
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    value = rekeningValue,
                    onValueChange = { newValue ->
                        rekeningValue = newValue.filter { it.isDigit() }.take(16)
                        if (rekeningValue.isNotEmpty()) rekeningError = null
                    },
                    labelText = "Nomor Rekening",
                    hintText = "Masukkan Nomor Rekening",
                    keyboardType = KeyboardType.Number,
                    maxLength = 16,
                    errorText = rekeningError,
                    showError = rekeningError != null,
                )
            }
        }
    }

    // 🔹 Sheet pilih bank tujuan
    if (showBankSheet) {
        AppModalBottomSheet(
            onDismissRequest = { closeBankSheet() },
            sheetState = bankSheetState,
        ) {
            PilihBankSheet(
                onDismiss = { closeBankSheet() },
                onBankSelected = { code, name ->
                    selectedBankCode = code
                    selectedBankName = name
                    bankError = null
                    closeBankSheet()
                },
            )
        }
    }

    // 🔹 Sheet detail rekening, muncul setelah "inquiry" (dummy) berhasil
    detailInquiry?.let { inquiry ->
        AppModalBottomSheet(
            onDismissRequest = { closeDetailSheet() },
            sheetState = detailSheetState,
        ) {
            DetailRekeningBaruModal(
                inquiry = inquiry,
                onDismiss = { closeDetailSheet() },
                onSaveRecipient = { alias -> /* ... */ },
                onContinue = { finalInquiry ->
                    closeDetailSheet {
                        onContinueToNextPage(finalInquiry)
                    }
                },
            )
        }
    }
}