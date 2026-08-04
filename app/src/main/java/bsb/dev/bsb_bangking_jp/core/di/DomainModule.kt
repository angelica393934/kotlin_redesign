package bsb.dev.bsb_bangking_jp.core.di

import bsb.dev.bsb_bangking_jp.domain.usecase.GetMeUseCase
import bsb.dev.bsb_bangking_jp.domain.usecase.LoginUseCase
import bsb.dev.bsb_bangking_jp.domain.usecase.RefreshTokenUseCase
import org.koin.dsl.module

/**
 * Modul domain: use case dibuat baru tiap diminta (factory), karena use case
 * tidak menyimpan state -- beda dengan Repository yang cocok jadi singleton.
 */
val domainModule = module {
    factory { LoginUseCase(get()) }
    factory { GetMeUseCase(get()) }
    factory { RefreshTokenUseCase(get()) }
}
