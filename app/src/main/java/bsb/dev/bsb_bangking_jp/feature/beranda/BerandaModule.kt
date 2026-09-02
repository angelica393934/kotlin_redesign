package bsb.dev.bsb_bangking_jp.feature.beranda

import bsb.dev.bsb_bangking_jp.feature.beranda.data.BerandaApiService
import bsb.dev.bsb_bangking_jp.feature.beranda.data.get_banner.GetBannerRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.get_banner.GetBannerRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.presentation.BerandaViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val BerandaModule = module {
    single { get<Retrofit>().create(BerandaApiService::class.java) }
    single<GetBannerRepository> { GetBannerRepositoryImpl(get(), get()) }

    // 🔹 single, BUKAN viewModel -- state banner tetap bertahan lintas navigasi.
    single { BerandaViewModel(get(), get(), get(), get()) }}