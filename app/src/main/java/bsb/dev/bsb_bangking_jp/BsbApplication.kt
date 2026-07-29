package bsb.dev.bsb_bangking_jp

import android.app.Application
import bsb.dev.bsb_bangking_jp.core.di.appModules
import bsb.dev.bsb_bangking_jp.core.session.SessionManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BsbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)

        startKoin {
            androidContext(this@BsbApplication)
            modules(appModules)
        }
    }
}
