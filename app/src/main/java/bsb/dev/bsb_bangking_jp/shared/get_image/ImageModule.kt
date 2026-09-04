package bsb.dev.bsb_bangking_jp.shared.get_image

import bsb.dev.bsb_bangking_jp.shared.get_image.domain.ImageRepository
import bsb.dev.bsb_bangking_jp.shared.get_image.data.ImageApiService
import bsb.dev.bsb_bangking_jp.shared.get_image.data.ImageRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val imageModule = module {
    single { get<Retrofit>().create(ImageApiService::class.java) }
    single<ImageRepository> { ImageRepositoryImpl(get(), get()) }
}