package com.olleh.gyme.gym.viewModel

import android.arch.lifecycle.MutableLiveData
import com.architecture.kotlinmvvm.base.AppViewModel
import com.architecture.kotlinmvvm.dagger.AppModule
import com.architecture.kotlinmvvm.database.LocalRepository
import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import com.architecture.kotlinmvvm.network.ErrorHandler
import com.architecture.kotlinmvvm.network.RemoteRepository
import com.architecture.kotlinmvvm.network.Resource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class GymViewModel @Inject constructor(

        errorHandler: ErrorHandler,
        private val localRepository: LocalRepository,
        @Named(AppModule.REMOTE_REPOSITORY) val remoteRepository: RemoteRepository

) : AppViewModel(errorHandler) {

    val equipments: MutableLiveData<Resource<List<Equipment>>> = MutableLiveData()
    val gym: MutableLiveData<Resource<Gym>> = MutableLiveData()

    var gymId: String = ""

    fun getGym() {

        mCompositeDisposable.add(

                localRepository
                        .getGymById(gymId)
                        .compose(applySchedulers())
                        .subscribe(
                                {
                                    gym.value = Resource.success(it)
                                },
                                {
                                    Timber.e(it)
                                    val state = errorHandler.extractErrorState(it)
                                    gym.value = Resource.error(state)
                                }
                        )
        )
    }

    fun getEquipments() {

        mCompositeDisposable.add(

                localRepository
                        .getEquipmentsById(gymId)
                        .compose(applySchedulers())
                        .subscribe(
                                {
                                    equipments.value = Resource.success(it)
                                },
                                {

                                    Timber.e(it)
                                    val state = errorHandler.extractErrorState(it)
                                    equipments.value = Resource.error(state)
                                }
                        )
        )
    }
}