package bsb.dev.bsb_bangking_jp.feature.transfer

import bsb.dev.bsb_bangking_jp.feature.transfer.data.daftar_bank.DaftarBankApiService
import bsb.dev.bsb_bangking_jp.feature.transfer.data.daftar_bank.DaftarBankLocalStore
import bsb.dev.bsb_bangking_jp.feature.transfer.data.daftar_bank.DaftarBankRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.transfer.data.last_transfer.LastTransferRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.transfer.data.saved_recipient.SavedRecipientApiService
import bsb.dev.bsb_bangking_jp.feature.transfer.data.saved_recipient.SavedRecipientRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.transfer.data.transfer.TransferApiService
import bsb.dev.bsb_bangking_jp.feature.transfer.data.transfer.TransferRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.daftar_bank.DaftarBankRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.last_transfer.LastTransferRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.saved_recipient.SavedRecipientRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.transfer.TransferRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.presentation.daftar_bank.DaftarBankViewModel
import bsb.dev.bsb_bangking_jp.feature.transfer.presentation.last_transfer.LastTransferViewModel
import bsb.dev.bsb_bangking_jp.feature.transfer.presentation.saved_recipient.SavedRecipientViewModel
import bsb.dev.bsb_bangking_jp.feature.transfer.presentation.transfer.TransferViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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

    // -- flow saved recipient (daftar tersimpan) --
    single { get<Retrofit>().create(SavedRecipientApiService::class.java) }
    single<SavedRecipientRepository> { SavedRecipientRepositoryImpl(get(), get()) }
    viewModel { SavedRecipientViewModel(get()) }

    // -- flow LastTransfer (last Transfer) --
    single<LastTransferRepository> {
        LastTransferRepositoryImpl(
            apiHelper = get(),
            rekeningRepository = get()
        )
    }

    viewModel {
        LastTransferViewModel(
            repository = get()
        )
    }
}