package bsb.dev.bsb_bangking_jp.core.di

import bsb.dev.bsb_bangking_jp.data.repository.AuthRepository
import org.koin.dsl.module

/**
 * Modul repository: satu instance AuthRepository dipakai bersama
 * (Singleton), menerima AuthApiService dari networkModule lewat get().
 */
val repositoryModule = module {
    single { AuthRepository(get()) }
}
