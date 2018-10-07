package com.architecture.kotlinmvvm.base

import com.architecture.kotlinmvvm.network.ErrorHandler

open class AppViewModel(open val errorHandler: ErrorHandler) : BaseViewModel()
