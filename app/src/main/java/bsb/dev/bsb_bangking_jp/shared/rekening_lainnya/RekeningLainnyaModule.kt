package bsb.dev.bsb_bangking_jp.shared.rekening_lainnya

import bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.data.RekeningLainnyaApiService
import bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.data.RekeningLainnyaRepositoryImpl
import bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.domain.RekeningLainnyaRepository
import bsb.dev.bsb_bangking_jp.shared.rekening_lainnya.presentation.RekeningLainnyaViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val rekeningLainnyaModule = module {
    single { get<Retrofit>().create(RekeningLainnyaApiService::class.java) }
    single<RekeningLainnyaRepository> { RekeningLainnyaRepositoryImpl(get(), get()) }
    single { RekeningLainnyaViewModel(get()) }
}