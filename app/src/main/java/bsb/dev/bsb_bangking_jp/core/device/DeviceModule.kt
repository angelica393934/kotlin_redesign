package bsb.dev.bsb_bangking_jp.core.device

import org.koin.dsl.module

val deviceModule = module {
    single { SecureStorageService(get()) }
    single { AppPreferences(get()) }
}