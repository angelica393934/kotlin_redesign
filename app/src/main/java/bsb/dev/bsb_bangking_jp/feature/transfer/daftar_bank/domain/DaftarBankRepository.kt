// feature/transfer/domain/DaftarBankRepository.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.daftar_bank.domain

interface DaftarBankRepository {
    suspend fun getDaftarBank(forceRefresh: Boolean = false): List<BankItem>
}