package com.architecture.kotlinmvvm.dagger

import android.app.Application
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.content.SharedPreferences
import com.architecture.kotlinmvvm.Secret
import com.architecture.kotlinmvvm.database.AppDatabase
import com.architecture.kotlinmvvm.database.LocalRepository
import com.architecture.kotlinmvvm.database.RoomLocalRepository
import com.architecture.kotlinmvvm.manager.DataManager
import com.architecture.kotlinmvvm.manager.GovDataManager
import com.architecture.kotlinmvvm.manager.SecureTokenManager
import com.architecture.kotlinmvvm.manager.TokenManager
import com.architecture.kotlinmvvm.model.OAuthToken
import com.architecture.kotlinmvvm.network.API
import com.architecture.kotlinmvvm.network.DuoRemoteRepository
import com.architecture.kotlinmvvm.network.RemoteRepository
import com.architecture.kotlinmvvm.network.RetrofitRemoteRepository
import com.architecture.kotlinmvvm.preferences.ConcealPreferences
import com.architecture.kotlinmvvm.preferences.PreferencesRepository
import com.architecture.kotlinmvvm.preferences.SecurePreferencesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AppModule {

    companion object {

        const val REMOTE_REPOSITORY = "remote_repository"
        const val OFFLINE_REMOTE_REPOSITORY = "offline_remote_repository"
        const val PREFERENCES_FILE = "preference_file"
    }

    @Provides
    @AppScope
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    @AppScope
    fun provideSharedPreferences(context: Context, @Named(PREFERENCES_FILE) prefsFile: String): SharedPreferences {
        return ConcealPreferences(context, prefsFile, Secret.lock())
    }

    @Provides
    @AppScope
    fun providePreferencesRepository(sharedPreferences: SharedPreferences): PreferencesRepository {
        return SecurePreferencesRepository(sharedPreferences)
    }

    @Provides
    @AppScope
    fun provideTokenManager(preferencesRepository: PreferencesRepository): TokenManager<OAuthToken> {
        return SecureTokenManager(preferencesRepository)
    }

    @Provides
    @AppScope
    @Named(REMOTE_REPOSITORY)
    fun provideRetrofitRemoteRepository(api: API): RemoteRepository {
        return RetrofitRemoteRepository(api)
    }

    @Provides
    @AppScope
    @Named(OFFLINE_REMOTE_REPOSITORY)
    fun provideDuoRemoteRepository(@Named(REMOTE_REPOSITORY) remoteRepository: RemoteRepository,
                                   localRepository: LocalRepository): RemoteRepository {
        return DuoRemoteRepository(remoteRepository, localRepository)
    }


    @Provides
    @AppScope
    fun provideDatabase(context: Context): AppDatabase = Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, "database")
            .build()

    @Provides
    @AppScope
    fun provideLocalRepository(database: AppDatabase): LocalRepository = RoomLocalRepository(database)

    @Provides
    @AppScope
    fun provideDataManager(@Named(REMOTE_REPOSITORY) remoteRepository: RemoteRepository,
                           localRepository: LocalRepository): DataManager {

        return GovDataManager(localRepository, remoteRepository)
    }
}
