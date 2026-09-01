package bsb.dev.bsb_bangking_jp.app

import android.app.Application
import bsb.dev.bsb_bangking_jp.app.di.sessionModule
import bsb.dev.bsb_bangking_jp.core.device.DeviceContext
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.device.deviceModule
import bsb.dev.bsb_bangking_jp.core.get_image.imageModule
import bsb.dev.bsb_bangking_jp.core.network.networkModule
import bsb.dev.bsb_bangking_jp.core.notification.NotificationHelper
import bsb.dev.bsb_bangking_jp.feature.aktivitas.aktivitasModule
import bsb.dev.bsb_bangking_jp.feature.beranda.BerandaModule
import bsb.dev.bsb_bangking_jp.feature.init.initModule
import bsb.dev.bsb_bangking_jp.feature.login.loginModule
import bsb.dev.bsb_bangking_jp.feature.login_existing.loginExistingModule
import bsb.dev.bsb_bangking_jp.feature.news.newsModule
import bsb.dev.bsb_bangking_jp.feature.splash.splashModule
import bsb.dev.bsb_bangking_jp.feature.transfer.transferModule
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
                loginModule,
                BerandaModule,
                aktivitasModule,
                transferModule,
                imageModule,
                newsModule,
                sessionModule,
                // tambahkan module fitur lain di sini
            )
        }

        // DeviceContext perlu SecureStorageService dari Koin, jadi diinit setelah startKoin
        DeviceContext.init(this, get<SecureStorageService>())
        NotificationHelper.createNotificationChannel(this) //untuk notif
    }
}