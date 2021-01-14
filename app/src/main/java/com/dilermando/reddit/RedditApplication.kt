package com.dilermando.reddit

import androidx.multidex.MultiDexApplication
import com.dilermando.reddit.data.logger.LoggerTree
import com.dilermando.reddit.domain.di.AppComponent
import com.dilermando.reddit.domain.di.AppModule
import com.dilermando.reddit.domain.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class RedditApplication : MultiDexApplication() {

    var appComponent: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        setupCalligraphy()
        createAppComponent()
        setupLeakCanary()
    }

    private fun setupTimber() {
//        Fabric.with(this, Crashlytics())
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(LoggerTree())
        }
    }

    private fun setupCalligraphy() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    private fun setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    private fun createAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

}
