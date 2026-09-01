package bsb.dev.bsb_bangking_jp.feature.beranda

import bsb.dev.bsb_bangking_jp.feature.beranda.data.BerandaApiService
import bsb.dev.bsb_bangking_jp.feature.beranda.data.profile.ProfileRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.data.rekening_lainnya.RekeningLainnyaRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.data.get_banner.GetBannerRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.profile.ProfileRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.RekeningLainnyaRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.get_banner.GetBannerRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.presentation.BerandaViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val BerandaModule = module {
    single { get<Retrofit>().create(BerandaApiService::class.java) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(),get()) }
    single<RekeningLainnyaRepository> { RekeningLainnyaRepositoryImpl(get(),get()) }
    single<GetBannerRepository> { GetBannerRepositoryImpl(get(), get()) }


    // 🔹 single, BUKAN viewModel -- harus 1 instance untuk seluruh app (cache bertahan lintas layar)
    single { BerandaViewModel(get(), get(), get(),  get()) }
}