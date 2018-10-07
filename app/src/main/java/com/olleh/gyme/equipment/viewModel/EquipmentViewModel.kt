package com.olleh.gyme.equipment.viewModel

import com.architecture.kotlinmvvm.base.AppViewModel
import com.architecture.kotlinmvvm.dagger.AppModule
import com.architecture.kotlinmvvm.database.LocalRepository
import com.architecture.kotlinmvvm.network.ErrorHandler
import com.architecture.kotlinmvvm.network.RemoteRepository
import javax.inject.Inject
import javax.inject.Named

class EquipmentViewModel @Inject constructor(errorHandler: ErrorHandler,
                                             private val localRepository: LocalRepository,
                                             @Named(AppModule.REMOTE_REPOSITORY) val remoteRepository: RemoteRepository)
    : AppViewModel(errorHandler) {


}