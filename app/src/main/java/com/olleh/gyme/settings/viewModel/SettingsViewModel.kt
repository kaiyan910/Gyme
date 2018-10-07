package com.olleh.gyme.settings.viewModel

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
import com.olleh.gyme.splashing.viewModel.SplashingViewModel
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import org.joda.time.DateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class SettingsViewModel @Inject constructor(errorHandler: ErrorHandler,
                                            private val preferencesRepository: PreferencesRepository,
                                            private val localRepository: LocalRepository,
                                            private val dataManager: DataManager,
                                            @Named(AppModule.REMOTE_REPOSITORY) val remoteRepository: RemoteRepository) : AppViewModel(
        errorHandler) {

    val lastUpdateTime: MutableLiveData<Resource<String>> = MutableLiveData()
    val updatedTime: MutableLiveData<Resource<String>> = MutableLiveData()

    init {

        lastUpdateTime.value = Resource.success(
                DateTime(preferencesRepository.getLastDownloadTime()).toString("dd-MM-yyyy HH:mm:ss"))
    }

    fun download() {

        mCompositeDisposable.add(


                dataManager.clean()
                        .flatMap {

                            dataManager.download()
                        }
                        .compose(applySchedulers())
                        .subscribe(

                                {

                                    val now = DateTime.now()

                                    preferencesRepository.setLastDownloadTime(now.millis)
                                    updatedTime.value = Resource.success(now.toString("dd/MM/yyyy HH:mm:ss"))
                                },

                                {

                                    Timber.e(it)
                                    val state = errorHandler.extractErrorState(it)
                                    updatedTime.value = Resource.error(state)
                                }
                        )
        )
    }
}