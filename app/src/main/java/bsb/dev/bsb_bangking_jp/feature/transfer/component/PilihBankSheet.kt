package bsb.dev.bsb_bangking_jp.feature.transfer.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import bsb.dev.bsb_bangking_jp.core.component.EmptyState
import bsb.dev.bsb_bangking_jp.core.component.SearchTextField
import bsb.dev.bsb_bangking_jp.core.dummy.DummyBank
import bsb.dev.bsb_bangking_jp.core.dummy.DummyData
import bsb.dev.bsb_bangking_jp.core.util.InitialName
import bsb.dev.bsb_bangking_jp.core.widgets.InitialAvatar


@Composable
fun PilihBankSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onBankSelected: (code: String, name: String) -> Unit,
) {
    var query by remember { mutableStateOf("") }

    val sesamaBank = remember { DummyData.bankList.filter { it.bankCode == "120" } }
    val bankLain = remember { DummyData.bankList.filter { it.bankCode != "120" } }

    val filteredSesama = remember(query) {
        sesamaBank.filter { it.bankName.contains(query, ignoreCase = true) }
    }
    val filteredBankLain = remember(query) {
        bankLain.filter { it.bankName.contains(query, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 1000.dp),
    ) {
        SearchTextField(
            value = query,
            onValueChange = { query = it },
            hintText = "Cari Bank Tujuan",
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (filteredSesama.isEmpty() && filteredBankLain.isEmpty()) {
            EmptyState(
                message = "Bank tidak ditemukan",
                subMessage = "Coba kata kunci lain",
                actionText = null,
                modifier = Modifier.height(300.dp),
            )
        } else {
            LazyColumn() {
                if (filteredSesama.isNotEmpty()) {
                    item {
                        Text("Sesama Bank", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    items(filteredSesama, key = { it.bankCode }) { bank ->
                        BankItem(bank = bank, onClick = { onBankSelected(bank.bankCode, bank.bankName) })
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                }

                if (filteredBankLain.isNotEmpty()) {
                    item {
                        Text("Daftar Bank Lain", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    items(filteredBankLain, key = { it.bankCode }) { bank ->
                        BankItem(bank = bank, onClick = { onBankSelected(bank.bankCode, bank.bankName) })
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BankItem(
    bank: DummyBank,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        InitialAvatar(
            initials = bank.bankName,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = bank.bankName,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}