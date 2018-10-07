package com.olleh.gyme.main.viewModel

import com.architecture.kotlinmvvm.base.AppViewModel
import com.architecture.kotlinmvvm.dagger.AppModule
import com.architecture.kotlinmvvm.network.ErrorHandler
import com.architecture.kotlinmvvm.network.RemoteRepository
import com.olleh.gyme.R
import javax.inject.Inject
import javax.inject.Named

class MainViewModel  @Inject constructor (override val errorHandler: ErrorHandler,
                                          @Named(AppModule.REMOTE_REPOSITORY) val remoteRepository: RemoteRepository) : AppViewModel(errorHandler) {


    var currentFragmentId = R.id.menu_map
}