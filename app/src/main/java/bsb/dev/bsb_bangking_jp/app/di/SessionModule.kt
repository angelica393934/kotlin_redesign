package bsb.dev.bsb_bangking_jp.app.di


import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.session.SessionClearer
import bsb.dev.bsb_bangking_jp.core.get_image.domain.ImageRepository
import bsb.dev.bsb_bangking_jp.feature.aktivitas.domain.ActivityHistoryRepository
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.get_banner.GetBannerRepository
import bsb.dev.bsb_bangking_jp.feature.news.domain.AllNewsRepository
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsDetailRepository
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.domain.LastTransferRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.saved_recipient.domain.SavedRecipientRepository

import org.koin.dsl.module


val sessionModule = module {
    single {
        SessionClearer(
            repositories = listOf(
//                get<AccountRepository>() as ClearableRepository,     // domain.account (baru)
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
}