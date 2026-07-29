package bsb.dev.bsb_bangking_jp.core.di

import bsb.dev.bsb_bangking_jp.core.network.AuthApiService
import bsb.dev.bsb_bangking_jp.core.network.RetrofitClient
import org.koin.dsl.module

/**
 * Modul network: sediakan instance AuthApiService lewat Koin (Singleton),
 * dibangun dari RetrofitClient (Builder pattern untuk OkHttp/Retrofit).
 */
val networkModule = module {
    single<AuthApiService> { RetrofitClient.authApi }
}
