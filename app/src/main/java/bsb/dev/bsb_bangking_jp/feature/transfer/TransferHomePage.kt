package bsb.dev.bsb_bangking_jp.feature.transfer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.component.AccountTile
import bsb.dev.bsb_bangking_jp.core.component.AppButton
import bsb.dev.bsb_bangking_jp.core.component.AppHeader
import bsb.dev.bsb_bangking_jp.core.component.EmptyState
import bsb.dev.bsb_bangking_jp.core.component.SearchTextField
import bsb.dev.bsb_bangking_jp.core.dummy.DummyData
import bsb.dev.bsb_bangking_jp.core.dummy.DummyLastTransfer
import bsb.dev.bsb_bangking_jp.core.dummy.DummySavedRecipient
import bsb.dev.bsb_bangking_jp.core.theme.extendedColors
import bsb.dev.bsb_bangking_jp.core.util.InitialName

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TransferHomePage(
    navController: NavController,
    onBackClick: () -> Unit = {},
    onTransferSekarang: () -> Unit = {},
    onAturTerjadwalClick: () -> Unit = {},
    onSavedRecipientTap: (DummySavedRecipient) -> Unit = {},
    onLastTransferTap: (DummyLastTransfer) -> Unit = {},
    onEditAlias: (DummySavedRecipient) -> Unit = {},
) {
    var showRecent by remember { mutableStateOf(true) }
    var isDeleteMode by remember { mutableStateOf(false) }
    val selectedAccounts = remember { mutableStateListOf<String>() }
    var query by remember { mutableStateOf("") }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val lastTransferList = DummyData.lastTransferList
    val savedRecipientList = remember(query) {
        DummyData.savedRecipientList.filter {
            it.alias.contains(query, ignoreCase = true) ||
                    it.bankName.contains(query, ignoreCase = true) ||
                    it.accountNumber.contains(query)
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Transfer",
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical =10.dp)) {
                AppButton(
                    text = if (isDeleteMode) "Hapus Rekening Terpilih" else "Transfer Sekarang",
                    enabled = !(isDeleteMode && selectedAccounts.isEmpty()),
                    onClick = {
                        if (isDeleteMode) {
                            showDeleteConfirm = true
                        } else {
                            onTransferSekarang()
                        }
                    },
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Banner "Transfer Terjadwal"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable { onAturTerjadwalClick() }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.asset_terjadwal),
                    contentDescription = "Terjadwal",
                    modifier = Modifier.size(53.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Transfer terjadwal lebih mudah!",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Pantau dan kelola jadwal transfer sesuai kebutuhan anda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.extendedColors.textSecondary,
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp),
                )
            }

            // Tab "Transfer Terakhir" / "Daftar Tersimpan"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                TransferTab(
                    title = "Transfer Terakhir",
                    active = showRecent,
                    onClick = { showRecent = true },
                    modifier = Modifier.weight(1f),
                )
                TransferTab(
                    title = "Daftar tersimpan",
                    active = !showRecent,
                    onClick = { showRecent = false },
                    modifier = Modifier.weight(1f),
                )
            }

            if (!showRecent) {
                Column(modifier = Modifier.padding(horizontal = 24.dp))
                {
                    SearchTextField(
                        value = query,
                        onValueChange = { query = it },
                        hintText = "Cari Daftar Tersimpan",
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isDeleteMode = !isDeleteMode
                                selectedAccounts.clear()
                            },
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = if (isDeleteMode) Icons.Default.Close else Icons.Default.Delete,
                            contentDescription = null,
                            tint = if (isDeleteMode) MaterialTheme.extendedColors.danger else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isDeleteMode) "Batal" else "Hapus Daftar",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDeleteMode) MaterialTheme.extendedColors.danger else MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                if (showRecent) {
                    if (lastTransferList.isEmpty()) {
                        EmptyState(
                            message = "Belum ada riwayat transfer.",
                            subMessage = "Data transfer terakhir akan ditampilkan setelah Anda melakukan transaksi.",
                            actionText = null,
                        )
                    } else {
                        LazyColumn {
                            items(lastTransferList, key = { it.id }) { item ->
                                    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)){
                                    AccountTile(
                                        initials = item.nama,
                                        nama = item.nama,
                                        bank = item.bank,
                                        accountNumber = item.accountNumber,
                                        onTap = { onLastTransferTap(item) },
                                    )
                                }
                            }
                        }
                    }
                } else {
                    if (savedRecipientList.isEmpty()) {
                        EmptyState(
                            message = "Belum ada rekening tersimpan.",
                            subMessage = "Tambahkan rekening tersimpan untuk mempermudah transfer berikutnya.",
                            actionText = null,
                        )
                    } else {
                        LazyColumn {
                            items(savedRecipientList, key = { it.id }) { item ->
                                val isSelected = selectedAccounts.contains(item.id)
                                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                                    AccountTile(
                                        initials = item.alias,
                                        nama = item.alias,
                                        bank = item.bankName,
                                        accountNumber = item.accountNumber,
                                        isSelected = isSelected,
                                        showCheckbox = isDeleteMode,
                                        checkboxValue = isSelected,
                                        onCheckboxChanged = { checked ->
                                            if (checked) selectedAccounts.add(item.id)
                                            else selectedAccounts.remove(item.id)
                                        },
                                        onTap = {
                                            if (isDeleteMode) {
                                                if (isSelected) selectedAccounts.remove(item.id)
                                                else selectedAccounts.add(item.id)
                                            } else {
                                                onSavedRecipientTap(item)
                                            }
                                        },
                                        onEdit = if (!isDeleteMode) {
                                            { onEditAlias(item) }
                                        } else null,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Konfirmasi hapus rekening terpilih (padanan CustomModalConfirm di Flutter)
    if (showDeleteConfirm) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showDeleteConfirm = false },
            sheetState = sheetState,
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Hapus Rekening Terpilih?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rekening yang dihapus akan hilang dari daftar tersimpan dan tidak dapat dipulihkan. Lanjutkan menghapus rekening ini?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.extendedColors.textSecondary,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    AppButton(
                        text = "Batal",
                        outlined = true,
                        modifier = Modifier.weight(1f),
                        onClick = { showDeleteConfirm = false },
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    AppButton(
                        text = "Hapus",
                        backgroundColor = MaterialTheme.extendedColors.danger,
                        textColor = MaterialTheme.extendedColors.onDanger,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // TODO: panggil use case/ViewModel untuk hapus DummyData.savedRecipientList
                            // berdasarkan selectedAccounts, lalu refresh list.
                            selectedAccounts.clear()
                            isDeleteMode = false
                            showDeleteConfirm = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun TransferTab(
    title: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = if (active) MaterialTheme.extendedColors.textPrimary else MaterialTheme.extendedColors.textDisabled,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(3.dp)
                .width(120.dp)
                .background(
                    if (active) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent
                )
        )
    }
}