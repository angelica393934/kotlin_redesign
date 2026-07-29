package bsb.dev.bsb_bangking_jp.feature.transfer

import androidx.compose.runtime.Composable
import bsb.dev.bsb_bangking_jp.core.component.InputPinPage
import bsb.dev.bsb_bangking_jp.core.dummy.ConfirmTransferResult
import bsb.dev.bsb_bangking_jp.feature.transfer.component.PeriksaKembaliData
import java.util.Date

private const val DUMMY_CORRECT_PIN = "123456"

@Composable
fun PinTfPage(
    data: PeriksaKembaliData,
    onBack: () -> Unit,
    onBerhasilSegera: (ConfirmTransferResult) -> Unit,
    onBerhasilDijadwalkan: (ConfirmTransferResult) -> Unit,
) {
    InputPinPage(
        title = "Masukkan M-PIN",
        onBackClick = onBack,
        centerTitleWithBackButton = true,
        validator = { enteredPin ->
            // TODO: ganti dengan validasi PIN dari backend (mis. lewat
            // TransferEvent.confirmTransfer) begitu use case asli tersedia.
            if (enteredPin != DUMMY_CORRECT_PIN) {
                "PIN yang Anda masukkan salah"
            } else {
                null
            }
        },
        onPinComplete = { _ ->
            val result = data.result
            val isScheduled = result.isScheduled
            val biayaLayananInt = result.biayaLayanan.filter { it.isDigit() }.toIntOrNull() ?: 0

            val confirmResult = ConfirmTransferResult(
                reffNum = "TRX${System.currentTimeMillis()}",
                transactionDate = Date(),
                beneficiaryName = data.penerimaName,
                beneficiaryBankName = data.penerimaBank,
                beneficiaryAccountNo = data.penerimaAccountNumber,
                senderName = result.sumber.name,
                senderAccountNo = result.sumber.number,
                amount = result.jumlah,
                adminFee = biayaLayananInt,
                totalDebit = result.jumlah + biayaLayananInt,
                remark = result.keterangan.ifEmpty { null },
                scheduleType = if (isScheduled) "SCHEDULED" else "IMMEDIATE",
                frequency = if (isScheduled) {
                    if (result.frekuensi.equals("Sekali", ignoreCase = true)) "ONCE" else "MONTHLY"
                } else {
                    null
                },
                scheduleDate = if (isScheduled) result.tanggal.ifEmpty { null } else null,
                startMonth = if (isScheduled) result.mulai.ifEmpty { null } else null,
                endMonth = if (isScheduled) result.sampai.ifEmpty { null } else null,
            )

            if (isScheduled) {
                onBerhasilDijadwalkan(confirmResult)
            } else {
                onBerhasilSegera(confirmResult)
            }
        },
    )
}