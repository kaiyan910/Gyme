package com.olleh.gyme.equipment.viewModel

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

class EquipmentSearchResultViewModel @Inject constructor(errorHandler: ErrorHandler,
                                                         private val localRepository: LocalRepository,
                                                         @Named(AppModule.REMOTE_REPOSITORY) val remoteRepository: RemoteRepository)
    : AppViewModel(errorHandler) {

    val equipmentList = MutableLiveData<Resource<List<Equipment>>>()
    val gymList = MutableLiveData<Resource<List<Gym>>>()

    var equipmentIds: MutableList<String> = mutableListOf()

    fun removeEquipment(id: String) {

        equipmentIds.remove(id)
        getGyms()
    }

    fun getGyms() {

        mCompositeDisposable.add(

                localRepository
                        .getGymByMatchingAllEquipments(equipmentIds)
                        .compose(applySchedulers())
                        .subscribe(
                                {
                                    Timber.d("<DEBUG> $it")
                                    gymList.value = Resource.success(it)
                                },
                                {
                                    Timber.e(it)
                                    val state = errorHandler.extractErrorState(it)
                                    gymList.value = Resource.error(state)
                                }
                        )
        )
    }

    fun getEquipments() {

        mCompositeDisposable.add(

                localRepository
                        .getEquipmentsByIds(equipmentIds)
                        .compose(applySchedulers())
                        .subscribe(
                                {
                                    equipmentList.value = Resource.success(it)
                                },
                                {
                                    val state = errorHandler.extractErrorState(it)
                                    equipmentList.value = Resource.error(state)
                                }
                        )
        )
    }
}