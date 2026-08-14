package bsb.dev.bsb_bangking_jp.feature.aktivitas

import bsb.dev.bsb_bangking_jp.feature.aktivitas.data.ActivityHistoryApiService
import bsb.dev.bsb_bangking_jp.feature.aktivitas.data.ActivityHistoryRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityHistoryRepository
import bsb.dev.bsb_bangking_jp.feature.aktivitas.presentation.ActivityHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val aktivitasModule = module {
    single { get<Retrofit>().create(ActivityHistoryApiService::class.java) }
    single<ActivityHistoryRepository> { ActivityHistoryRepositoryImpl(get(), get()) }
    viewModel { ActivityHistoryViewModel(get()) }
}