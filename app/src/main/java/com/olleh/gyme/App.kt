package com.olleh.gyme

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDex
import com.architecture.kotlinmvvm.base.BaseApp
import com.architecture.kotlinmvvm.utils.LocaleUtils
import com.architecture.kotlinmvvm.utils.LogcatStrategy
import com.olleh.gyme.dagger.DaggerAppComponent
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber
import javax.inject.Inject

class App: BaseApp(), HasActivityInjector {

    @Inject
    lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = mActivityInjector

    override fun onCreate() {

        super.onCreate()

        DaggerAppComponent
                .builder()
                .app(this)
                .endpoint("http://www.lcsd.gov.hk")
                .preferencesFile("secure")
                .build()
                .inject(this)

        setupLogger()
    }

    private fun setupLogger() {

        val strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .logStrategy(LogcatStrategy())
                .methodCount(0)
                .build()

        Logger.addLogAdapter(AndroidLogAdapter(strategy))

        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                Logger.log(priority, tag, message, t)
            }
        })

        JodaTimeAndroid.init(this)
    }

    override fun attachBaseContext(base: Context) {

        super.attachBaseContext(LocaleUtils.onAttach(base))
        MultiDex.install(this)
    }

}