// feature/transfer/presentation/TransferContract.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.presentation

import bsb.dev.bsb_bangking_jp.feature.transfer.domain.TransferInquiry
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.TransferPurpose

data class TransferUiState(
    val isInquiryLoading: Boolean = false,
    val inquiryError: String? = null, // dipakai untuk field rekening (respCode "0602")

    val isSavingRecipient: Boolean = false,
    val saveRecipientError: String? = null,

    val isSubmittingTransfer: Boolean = false,
    val transferError: String? = null,

    val isConfirming: Boolean = false,
    val confirmError: String? = null,

    val transferPurposes: List<TransferPurpose> = emptyList(),
)

sealed class TransferNavEvent {
    data class ToDetailRekening(val inquiry: TransferInquiry) : TransferNavEvent()
    object RecipientSaved : TransferNavEvent()
    object TransferSubmitted : TransferNavEvent() // lanjut ke halaman PIN
    data class ConfirmSuccess(
        val result: bsb.dev.bsb_bangking_jp.core.dummy.ConfirmTransferResult,
    ) : TransferNavEvent()
}

sealed class TransferUiEvent {
    data class ShowToastError(val message: String) : TransferUiEvent()
}