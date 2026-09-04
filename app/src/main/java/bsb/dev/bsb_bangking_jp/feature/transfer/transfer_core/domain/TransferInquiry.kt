package bsb.dev.bsb_bangking_jp.feature.transfer.transfer_core.domain

import bsb.dev.bsb_bangking_jp.core.util.InitialName

/** Padanan TransferInquiryModel di  -- hasil dari getaccountdest. */
data class TransferInquiry(
    val bankName: String,
    val beneficiaryName: String,
    val beneficiaryAccountNo: String,
    val beneficiaryAccountType: String,
    val currency: String,
    val supportedServices: List<String>,
) {
    /** Padanan getter `initials`. */
    val initials: String
        get() = beneficiaryName.InitialName()

    /** Padanan getter `isOnUs` -- true = transfer sesama BSB. */
    val isOnUs: Boolean
        get() = supportedServices.contains("TRANSFER_ONUS")
}