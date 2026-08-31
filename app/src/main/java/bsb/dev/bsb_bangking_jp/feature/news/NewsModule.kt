package bsb.dev.bsb_bangking_jp.feature.news

import bsb.dev.bsb_bangking_jp.feature.news.data.AllNewsRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.news.data.NewsApiService
import bsb.dev.bsb_bangking_jp.feature.news.data.NewsDetailRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.news.data.NewsRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.news.domain.AllNewsRepository
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsDetailRepository
import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsRepository
import bsb.dev.bsb_bangking_jp.feature.news.presentation.AllNewsViewModel
import bsb.dev.bsb_bangking_jp.feature.news.presentation.NewsDetailViewModel
import bsb.dev.bsb_bangking_jp.feature.news.presentation.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val newsModule = module {
    single { get<Retrofit>().create(NewsApiService::class.java) }

    single<NewsRepository> { NewsRepositoryImpl(get(), get()) }
    single<AllNewsRepository> { AllNewsRepositoryImpl(get(), get()) }
    single<NewsDetailRepository> { NewsDetailRepositoryImpl(get()) } // GetWithBodyApiHelper sudah single

    // viewModel biasa -- state UI tidak perlu bertahan lintas route,
    // cache betulan disimpan di repository (single) di atas.
    viewModel { NewsViewModel(get()) }
    viewModel { AllNewsViewModel(get()) }
    viewModel { NewsDetailViewModel(get()) }
}