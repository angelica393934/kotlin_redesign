package bsb.dev.bsb_bangking_jp.feature.transfer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import bsb.dev.bsb_bangking_jp.core.dummy.DummyData
import bsb.dev.bsb_bangking_jp.core.dummy.DummyRekening
import bsb.dev.bsb_bangking_jp.feature.transfer.component.RekeningSumberUiState


@Composable
fun TransferBSBPage(
    bank: String,
    accountNumber: String,
    name: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onLanjutkan: (TransferFormResult) -> Unit = {},
) {
    TransferFormPage(
        jenis = TransferJenis.SESAMA_BSB,
        bank = bank,
        accountNumber = accountNumber,
        name = name,
        modifier = modifier,
        onBack = onBack,
        onLanjutkan = onLanjutkan,
    )
}