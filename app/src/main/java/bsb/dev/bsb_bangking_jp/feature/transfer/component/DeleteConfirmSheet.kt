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
import bsb.dev.bsb_bangking_jp.core.component.AppButton
import bsb.dev.bsb_bangking_jp.core.component.AppModalBottomSheet
import bsb.dev.bsb_bangking_jp.core.theme.extendedColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AppModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
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

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppButton(
                        text = "Batal",
                        outlined = true,
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    AppButton(
                        text = "Hapus",
                        backgroundColor = MaterialTheme.extendedColors.danger,
                        textColor = MaterialTheme.extendedColors.onDanger,
                        modifier = Modifier.weight(1f),
                        onClick = onConfirm,
                    )
                }
            }
        }
    }
}