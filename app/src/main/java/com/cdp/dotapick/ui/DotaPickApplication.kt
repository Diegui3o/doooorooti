package com.cdp.dotapick

import android.app.Application
import com.cdp.dotapick.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DotaPickApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DotaPickApplication)
            modules(appModule)
        }
    }
}