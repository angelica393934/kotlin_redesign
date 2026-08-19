package bsb.dev.bsb_bangking_jp.feature.aktivitas

import bsb.dev.bsb_bangking_jp.feature.aktivitas.data.ActivityHistoryRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityHistoryRepository
import bsb.dev.bsb_bangking_jp.feature.aktivitas.presentation.ActivityHistoryViewModel
import org.koin.dsl.module

val aktivitasModule = module {
    single<ActivityHistoryRepository> { ActivityHistoryRepositoryImpl(get()) }
    single { ActivityHistoryViewModel(get(), get()) }
}