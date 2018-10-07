package com.olleh.gyme.equipment.viewModel

import android.arch.lifecycle.MutableLiveData
import com.architecture.kotlinmvvm.base.AppViewModel
import com.architecture.kotlinmvvm.dagger.AppModule
import com.architecture.kotlinmvvm.database.LocalRepository
import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.network.ErrorHandler
import com.architecture.kotlinmvvm.network.RemoteRepository
import com.architecture.kotlinmvvm.network.Resource
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class EquipmentListViewModel @Inject constructor(errorHandler: ErrorHandler,
                                                 private val localRepository: LocalRepository,
                                                 @Named(AppModule.REMOTE_REPOSITORY) val remoteRepository: RemoteRepository)
    : AppViewModel(errorHandler) {

    val equipments = MutableLiveData<Resource<List<Equipment>>>()

    fun getEquipments(keyword: String) {

        mCompositeDisposable.add(

                localRepository
                        .getEquipmentsByKeyword("%$keyword%")
                        .compose(applySchedulers())
                        .subscribe(
                                { res ->

                                    equipments.value = Resource.success(
                                            res.sortedWith(compareBy { it.nameEN }))
                                },
                                {
                                    Timber.e(it)
                                    val state = errorHandler.extractErrorState(it)
                                    equipments.value = Resource.error(state)
                                }
                        )
        )
    }

    fun getEquipments() {

        mCompositeDisposable.add(

                localRepository
                        .getEquipments()
                        .compose(applySchedulers())
                        .subscribe(
                                { res ->

                                    equipments.value = Resource.success(
                                            res.sortedWith(compareBy { it.nameEN }))
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