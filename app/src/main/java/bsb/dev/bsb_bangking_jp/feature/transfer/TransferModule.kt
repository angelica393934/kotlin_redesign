// feature/transfer/TransferModule.kt
package bsb.dev.bsb_bangking_jp.feature.transfer

import bsb.dev.bsb_bangking_jp.feature.transfer.data.daftar_bank.DaftarBankApiService
import bsb.dev.bsb_bangking_jp.feature.transfer.data.daftar_bank.DaftarBankLocalStore
import bsb.dev.bsb_bangking_jp.feature.transfer.data.daftar_bank.DaftarBankRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.daftar_bank.DaftarBankRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.presentation.daftar_bank.DaftarBankViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import bsb.dev.bsb_bangking_jp.feature.transfer.data.TransferApiService
import bsb.dev.bsb_bangking_jp.feature.transfer.data.TransferRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.TransferRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.presentation.TransferViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val transferModule = module {
    single { get<Retrofit>().create(DaftarBankApiService::class.java) }
    single { DaftarBankLocalStore(get()) }
    single<DaftarBankRepository> { DaftarBankRepositoryImpl(get(), get(), get()) }
    viewModel { DaftarBankViewModel(get()) }
// -- flow transfer --
    single { get<Retrofit>().create(TransferApiService::class.java) }
    single<TransferRepository> { TransferRepositoryImpl(get(), get()) }
    // 🔹 single, BUKAN viewModel -- state harus bertahan lintas route
    // (transfer_baru -> transfer_bsb/umum -> pin_transfer)
    single { TransferViewModel(get()) }

}