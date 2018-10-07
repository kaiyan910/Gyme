package com.olleh.gyme.map.viewModel

import android.arch.lifecycle.MutableLiveData
import com.architecture.kotlinmvvm.base.AppViewModel
import com.architecture.kotlinmvvm.dagger.AppModule
import com.architecture.kotlinmvvm.database.LocalRepository
import com.architecture.kotlinmvvm.model.Gym
import com.architecture.kotlinmvvm.network.ErrorHandler
import com.architecture.kotlinmvvm.network.RemoteRepository
import com.architecture.kotlinmvvm.network.Resource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class MapViewModel @Inject constructor(errorHandler: ErrorHandler,
                                       private val localRepository: LocalRepository,
                                       @Named(AppModule.REMOTE_REPOSITORY) val remoteRepository: RemoteRepository) : AppViewModel(errorHandler) {

    val gymLocations = MutableLiveData<Resource<List<Gym>>>()

    fun getGymLocation() {

        mCompositeDisposable.add(

                localRepository
                        .getGym()
                        .compose(applySchedulers())
                        .subscribe(
                                {
                                    gymLocations.value = Resource.success(it)
                                },
                                {
                                    Timber.e(it)
                                    val state = errorHandler.extractErrorState(it)
                                    gymLocations.value = Resource.error(state)
                                }
                        )
        )
    }

    fun getGymLocationByArea(area: String) {

        mCompositeDisposable.add(

                localRepository
                        .getGymByArea(area)
                        .compose(applySchedulers())
                        .subscribe(
                                {
                                    gymLocations.value = Resource.success(it)
                                },
                                {
                                    Timber.e(it)
                                    val state = errorHandler.extractErrorState(it)
                                    gymLocations.value = Resource.error(state)
                                }
                        )
        )

    }
}