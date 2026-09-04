package bsb.dev.bsb_bangking_jp.feature.message

import bsb.dev.bsb_bangking_jp.feature.message.data.MessageDetailRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.message.data.MessageHistoryRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.message.domain.MessageDetailRepository
import bsb.dev.bsb_bangking_jp.feature.message.domain.MessageHistoryRepository
import bsb.dev.bsb_bangking_jp.feature.message.presentation.MessageDetailViewModel
import bsb.dev.bsb_bangking_jp.feature.message.presentation.MessageHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val messageModule = module {
    single<MessageHistoryRepository> { MessageHistoryRepositoryImpl(get()) }
    single<MessageDetailRepository> { MessageDetailRepositoryImpl(get()) }

    // single, sama seperti ActivityHistoryViewModel -- state bertahan lintas navigasi
    single { MessageHistoryViewModel(get(), get()) }

    // viewModel biasa -- cuma dipakai saat bottom sheet detail dibuka
    viewModel { MessageDetailViewModel(get()) }
}