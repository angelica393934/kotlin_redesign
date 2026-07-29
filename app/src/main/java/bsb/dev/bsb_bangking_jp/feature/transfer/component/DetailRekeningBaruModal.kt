package bsb.dev.bsb_bangking_jp.feature.transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.core.component.AccountTile
import bsb.dev.bsb_bangking_jp.core.component.AppButton
import bsb.dev.bsb_bangking_jp.core.component.AppSwitch
import bsb.dev.bsb_bangking_jp.core.component.AppTextField
import bsb.dev.bsb_bangking_jp.core.dummy.DummyTransferInquiry
import bsb.dev.bsb_bangking_jp.core.theme.extendedColors

@Composable
fun DetailRekeningBaruModal(
    inquiry: DummyTransferInquiry,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onContinue: (DummyTransferInquiry) -> Unit,
    onSaveRecipient: (alias: String) -> Unit = {},
) {
    var isSaveReceiver by remember { mutableStateOf(false) }
    var alias by remember { mutableStateOf("") }
    var aliasError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Detail Rekening",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Pastikan Nama Penerima Telah Sesuai",
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            color = MaterialTheme.extendedColors.inputBackground
        )
        Spacer(modifier = Modifier.height(10.dp))

        AccountTile(
            initials = inquiry.initials,
            nama = inquiry.beneficiaryName,
            bank = inquiry.bankName,
            accountNumber = inquiry.beneficiaryAccountNo,
        )
        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            color = MaterialTheme.extendedColors.inputBackground
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically, // <-- ini kuncinya
        ) {
            Text(
                text = "Simpan Penerima",
                style = MaterialTheme.typography.titleLarge,
            )
            AppSwitch(
                checked = isSaveReceiver,
                onCheckedChange = { isSaveReceiver = it },
            )
        }

        if (isSaveReceiver) {
            Spacer(modifier = Modifier.height(10.dp))
            AppTextField(
                value = alias,
                onValueChange = {
                    alias = it
                    aliasError = null
                },
                labelText = "Simpan Sebagai",
                hintText = "Nama Inisial",
                icon = Icons.Default.Person,
                errorText = aliasError,
                showError = aliasError != null,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AppButton(
            text = "Lanjutkan",
            onClick = {
                if (!isSaveReceiver) {
                    onContinue(inquiry)
                } else {
                    val trimmedAlias = alias.trim()
                    if (trimmedAlias.isEmpty()) {
                        aliasError = "Nama inisial tidak boleh kosong"
                    } else {
                        onSaveRecipient(trimmedAlias)
                        onContinue(inquiry)
                    }
                }
            },
        )
    }
}