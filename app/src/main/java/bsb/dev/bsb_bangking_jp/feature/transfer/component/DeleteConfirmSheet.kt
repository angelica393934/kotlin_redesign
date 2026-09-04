package bsb.dev.bsb_bangking_jp.feature.transfer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.core.component.AppButton
import bsb.dev.bsb_bangking_jp.core.component.AppModalBottomSheet
import bsb.dev.bsb_bangking_jp.core.component.AppModalConfirm
import bsb.dev.bsb_bangking_jp.core.theme.extendedColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AppModalConfirm(
        onDismissRequest = onDismiss,
        title = "Hapus Rekening Terpilih?",
        description = "Rekening yang dihapus akan hilang dari daftar tersimpan dan tidak dapat dipulihkan.\n Lanjutkan menghapus rekening ini?",
        cancelText = "Batal",
        onCancel = onDismiss,
        confirmText = "Hapus",
        onConfirm = onConfirm,
    )
}