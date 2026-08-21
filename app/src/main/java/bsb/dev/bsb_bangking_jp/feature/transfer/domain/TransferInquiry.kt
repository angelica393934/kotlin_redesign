package bsb.dev.bsb_bangking_jp.feature.transfer.domain

/** Padanan TransferInquiryModel di Flutter -- hasil dari getaccountdest. */
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
        get() = beneficiaryName
            .trim()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
            .take(2)
            .mapNotNull { it.firstOrNull()?.toString() }
            .joinToString("")

    /** Padanan getter `isOnUs` -- true = transfer sesama BSB. */
    val isOnUs: Boolean
        get() = supportedServices.contains("TRANSFER_ONUS")
}