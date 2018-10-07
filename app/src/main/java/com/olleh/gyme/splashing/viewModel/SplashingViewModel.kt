package com.olleh.gyme.splashing.viewModel

import android.arch.lifecycle.MutableLiveData
import com.architecture.kotlinmvvm.base.AppViewModel
import com.architecture.kotlinmvvm.dagger.AppModule
import com.architecture.kotlinmvvm.database.LocalRepository
import com.architecture.kotlinmvvm.manager.DataManager
import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import com.architecture.kotlinmvvm.network.ErrorHandler
import com.architecture.kotlinmvvm.network.RemoteRepository
import com.architecture.kotlinmvvm.network.Resource
import com.architecture.kotlinmvvm.preferences.PreferencesRepository
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class SplashingViewModel @Inject constructor(errorHandler: ErrorHandler,
                                             private val preferencesRepository: PreferencesRepository,
                                             private val localRepository: LocalRepository,
                                             private val dataManager: DataManager,
                                             @Named(AppModule.REMOTE_REPOSITORY) val remoteRepository: RemoteRepository)
    : AppViewModel(errorHandler) {

    val downloadState = MutableLiveData<Resource<Boolean>>()

    fun download() {

        mCompositeDisposable.add(

                Flowable.just(true)
                        .delay(1, TimeUnit.SECONDS)
                        .zipWith(

                                Flowable
                                        .just(preferencesRepository.getLastDownloadTime())
                                        .flatMap { lastDownloadTime ->

                                            Timber.d("<DEBUG> lastDownloadTime=$lastDownloadTime")
                                            if (lastDownloadTime > 0L) {

                                                Flowable.just(true)

                                            } else {

                                                dataManager.download()
                                            }
                                        },

                                BiFunction { t1: Boolean, t2: Boolean -> t2 }

                        )
                        .compose(applySchedulers())
                        .subscribe(

                                {
                                    preferencesRepository.setLastDownloadTime(Date().time)
                                    downloadState.value = Resource.success(true)
                                },

                                {

                                    Timber.e(it)
                                    val state = errorHandler.extractErrorState(it)
                                    downloadState.value = Resource.error(state)
                                }
                        )
        )
    }

    data class DataWrapper(val gymRooms: List<Gym>, val equipments: List<Equipment>)
}