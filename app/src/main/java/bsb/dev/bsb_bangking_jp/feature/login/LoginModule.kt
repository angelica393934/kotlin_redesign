package bsb.dev.bsb_bangking_jp.feature.login

import bsb.dev.bsb_bangking_jp.feature.login.data.LoginRepositoryImpl
import bsb.dev.bsb_bangking_jp.feature.login.domain.LoginRepository
import bsb.dev.bsb_bangking_jp.feature.login.domain.LoginUseCase
import bsb.dev.bsb_bangking_jp.feature.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get(), get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get()) }
}