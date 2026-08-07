package bsb.dev.bsb_bangking_jp

import android.app.Application
import bsb.dev.bsb_bangking_jp.core.device.DeviceContext
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.device.deviceModule
import bsb.dev.bsb_bangking_jp.core.network.networkModule
import bsb.dev.bsb_bangking_jp.feature.init.initModule
import bsb.dev.bsb_bangking_jp.feature.login_existing.loginExistingModule
import bsb.dev.bsb_bangking_jp.feature.splash.splashModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BsbApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BsbApplication)
            modules(
                deviceModule,
                networkModule,
                initModule,
                splashModule,
                loginExistingModule,
                // tambahkan module fitur lain di sini
            )
        }

        // DeviceContext perlu SecureStorageService dari Koin, jadi diinit setelah startKoin
        DeviceContext.init(this, get<SecureStorageService>())
    }
}