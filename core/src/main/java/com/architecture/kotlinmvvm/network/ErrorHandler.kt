package com.architecture.kotlinmvvm.network

interface ErrorHandler {

    fun extractErrorState(throwable: Throwable): ErrorState
}