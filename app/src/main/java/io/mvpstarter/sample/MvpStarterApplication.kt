package io.mvpstarter.sample

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.singhajit.sherlock.core.Sherlock
import com.squareup.leakcanary.LeakCanary
import com.tspoon.traceur.Traceur
import io.mvpstarter.sample.injection.component.AppComponent
import io.mvpstarter.sample.injection.component.DaggerAppComponent
import io.mvpstarter.sample.injection.module.AppModule
import io.mvpstarter.sample.injection.module.NetworkModule
import timber.log.Timber
import cloud.mindbox.mobile_sdk.Mindbox

class MvpStarterApplication : MultiDexApplication() {

    private var appComponent: AppComponent? = null

    companion object {
        operator fun get(context: Context): MvpStarterApplication {
            return context.applicationContext as MvpStarterApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        
        val configuration = cloud.mindbox.mobile_sdk.MindboxConfiguration.Builder(
            this,
            "https://api.mindbox.ru",
            "B3XN9rTM1FCVz2hAXSyDtxTca50xL9Cj"
        ).build()
        
        cloud.mindbox.mobile_sdk.Mindbox.init(this, configuration, emptyList())

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
            LeakCanary.install(this)
            Sherlock.init(this)
            Traceur.enableLogging()
        }
    }

    // Needed to replace the component with a test specific one
    var component: AppComponent
        get() {
            if (appComponent == null) {
                appComponent = DaggerAppComponent.builder()
                        .appModule(AppModule(this))
                        .networkModule(NetworkModule(this))
                        .build()
            }
            return appComponent as AppComponent
        }
        set(appComponent) {
            this.appComponent = appComponent
        }

}
