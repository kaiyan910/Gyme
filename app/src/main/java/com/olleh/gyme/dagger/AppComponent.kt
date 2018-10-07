package com.olleh.gyme.dagger

import android.app.Application
import com.architecture.kotlinmvvm.dagger.AppModule
import com.architecture.kotlinmvvm.dagger.AppScope
import com.architecture.kotlinmvvm.dagger.NetworkModule
import com.olleh.gyme.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Named


@AppScope
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ViewModelModule::class,
    ActivityBuilderModule::class,
    NetworkModule::class,
    AppModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun app(app: Application): Builder

        @BindsInstance
        fun preferencesFile(@Named(AppModule.PREFERENCES_FILE) name: String): Builder

        @BindsInstance
        fun endpoint(@Named(NetworkModule.ENDPOINT) endpoint: String): Builder

        fun build(): AppComponent
    }
}
