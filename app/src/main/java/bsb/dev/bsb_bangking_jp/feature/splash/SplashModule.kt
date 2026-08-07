package bsb.dev.bsb_bangking_jp.feature.splash

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel {
        SplashViewModel(
            application = androidApplication(),
            initDeviceUseCase = get(),
            appPreferences = get(),
        )
    }
}