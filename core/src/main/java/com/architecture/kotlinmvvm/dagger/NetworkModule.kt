package com.architecture.kotlinmvvm.dagger

import com.architecture.kotlinmvvm.BuildConfig
import com.architecture.kotlinmvvm.network.API
import com.architecture.kotlinmvvm.network.BasicErrorHandler
import com.architecture.kotlinmvvm.network.ErrorHandler
import com.architecture.kotlinmvvm.network.RemoteRepository
import com.architecture.kotlinmvvm.network.RetrofitRemoteRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import com.squareup.moshi.Moshi



@Module
class NetworkModule {

    companion object {

        const val ENDPOINT = "endpoint"
    }

    @Provides
    @AppScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @AppScope
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient().newBuilder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(httpLoggingInterceptor)
                    }
                }
                .build()
    }

    @Provides
    @AppScope
    fun provideConvertFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Provides
    @AppScope
    fun provideRetrofit(@Named(ENDPOINT) server: String, okHttpClient: OkHttpClient,
                        factory: Converter.Factory): Retrofit {

        return Retrofit.Builder()
                .baseUrl(server)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @AppScope
    fun provideAPI(retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }

    @Provides
    @AppScope
    fun provideErrorHandler(): ErrorHandler {
        return BasicErrorHandler()
    }
}