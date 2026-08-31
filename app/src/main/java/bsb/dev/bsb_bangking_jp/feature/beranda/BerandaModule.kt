package bsb.dev.bsb_bangking_jp.feature.beranda

import bsb.dev.bsb_bangking_jp.core.get_image.domain.ImageRepository
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.session.SessionClearer
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityHistoryRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.data.BerandaApiService
import bsb.dev.bsb_bangking_jp.feature.beranda.data.profile.ProfileRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.data.rekening_lainnya.RekeningLainnyaRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.data.get_banner.GetBannerRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.profile.ProfileRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.RekeningLainnyaRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.get_banner.GetBannerRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.presentation.BerandaViewModel
import bsb.dev.bsb_bangking_jp.feature.news.domain.AllNewsRepository
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsDetailRepository
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.last_transfer.LastTransferRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.domain.saved_recipient.SavedRecipientRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val BerandaModule = module {
    single { get<Retrofit>().create(BerandaApiService::class.java) }

    single<ProfileRepository> { ProfileRepositoryImpl(get(),get()) }
    single<RekeningLainnyaRepository> { RekeningLainnyaRepositoryImpl(get(),get()) }
    single<GetBannerRepository> { GetBannerRepositoryImpl(get(), get()) }

    single {
        SessionClearer(
            repositories = listOf(
                get<ProfileRepository>() as ClearableRepository,
                get<RekeningLainnyaRepository>() as ClearableRepository,
                get<ActivityHistoryRepository>() as ClearableRepository,
                get<LastTransferRepository>() as ClearableRepository,
                get<SavedRecipientRepository>() as ClearableRepository,
                get<GetBannerRepository>() as ClearableRepository,
                get<NewsRepository>() as ClearableRepository,
                get<AllNewsRepository>() as ClearableRepository,
                get<NewsDetailRepository>() as ClearableRepository,
                get<ImageRepository>() as ClearableRepository,
            )
        )
    }

    // 🔹 single, BUKAN viewModel -- harus 1 instance untuk seluruh app (cache bertahan lintas layar)
    single { BerandaViewModel(get(), get(), get(),  get()) }
}