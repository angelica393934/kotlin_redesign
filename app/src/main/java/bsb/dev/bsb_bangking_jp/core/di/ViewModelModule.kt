package bsb.dev.bsb_bangking_jp.core.di

import bsb.dev.bsb_bangking_jp.feature.beranda.viewmodel.BerandaViewModel
import bsb.dev.bsb_bangking_jp.feature.portal.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Modul ViewModel: pakai `viewModel { ... }` (bukan `single`/`factory`) supaya
 * Koin yang mengurus lifecycle-nya lewat ViewModelStoreOwner, sama seperti
 * `viewModel()` bawaan Compose tapi dependency-nya di-resolve otomatis.
 *
 * Tambahkan ViewModel fitur lain di sini (mis. BerandaViewModel) begitu
 * fiturnya juga dipindah pakai Koin.
 */
val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { BerandaViewModel(get(), get()) }
}

/** Kumpulan semua modul, didaftarkan sekali di BsbApplication. */
val appModules = listOf(
    networkModule,
    repositoryModule,
    domainModule,
    viewModelModule,
)
