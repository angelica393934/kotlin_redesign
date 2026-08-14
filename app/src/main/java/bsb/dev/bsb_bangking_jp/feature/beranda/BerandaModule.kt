// feature/beranda/BerandaModule.kt
package bsb.dev.bsb_bangking_jp.feature.beranda

import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.session.SessionClearer
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityHistoryRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.data.BerandaApiService
import bsb.dev.bsb_bangking_jp.feature.beranda.data.ProfileRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.data.RekeningLainnyaRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.ProfileRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.RekeningLainnyaRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.presentation.BerandaViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val BerandaModule = module {
    single { get<Retrofit>().create(BerandaApiService::class.java) }

    single<ProfileRepository> { ProfileRepositoryImpl(get(),get()) }
    single<RekeningLainnyaRepository> { RekeningLainnyaRepositoryImpl(get(),get()) }

    single {
        SessionClearer(
            repositories = listOf(
                get<ProfileRepository>() as ClearableRepository,
                get<RekeningLainnyaRepository>() as ClearableRepository,
                get<ActivityHistoryRepository>() as ClearableRepository,)
        )
    }

    // 🔹 single, BUKAN viewModel -- harus 1 instance untuk seluruh app (cache bertahan lintas layar)
    single { BerandaViewModel(get(), get(), get()) }
}