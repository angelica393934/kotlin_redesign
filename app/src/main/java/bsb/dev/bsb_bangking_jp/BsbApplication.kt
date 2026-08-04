package bsb.dev.bsb_bangking_jp

import android.app.Application
import bsb.dev.bsb_bangking_jp.core.session.SessionManager

class BsbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)

//        startKoin {
//            androidContext(this@BsbApplication)
//            modules(appModules)
//        }
    }
}
