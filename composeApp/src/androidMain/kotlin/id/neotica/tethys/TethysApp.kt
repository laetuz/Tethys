package id.neotica.tethys

import android.app.Application
import id.neotica.tethys.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TethysApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TethysApp)
            modules(appModule)
        }
    }
}
